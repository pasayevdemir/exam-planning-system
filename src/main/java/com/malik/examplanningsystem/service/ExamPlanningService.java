package com.malik.examplanningsystem.service;

import com.malik.examplanningsystem.entity.Classroom;
import com.malik.examplanningsystem.entity.Exam;
import com.malik.examplanningsystem.entity.Instructor;
import com.malik.examplanningsystem.entity.Student;
import com.malik.examplanningsystem.exception.DuplicateResourceException;
import com.malik.examplanningsystem.exception.InsufficientCapacityException;
import com.malik.examplanningsystem.repository.ClassroomRepository;
import com.malik.examplanningsystem.repository.ExamAssignmentRepository;
import com.malik.examplanningsystem.repository.ExamRepository;
import com.malik.examplanningsystem.repository.InstructorRepository;
import com.malik.examplanningsystem.repository.InvigilatorAssignmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ExamPlanningService {

    private final ExamService examService;
    private final StudentService studentService;
    private final ExamAssignmentService examAssignmentService;
    private final InvigilatorAssignmentService invigilatorAssignmentService;
    private final ClassroomRepository classroomRepository;
    private final ExamRepository examRepository;
    private final ExamAssignmentRepository examAssignmentRepository;
    private final InvigilatorAssignmentRepository invigilatorAssignmentRepository;
    private final InstructorRepository instructorRepository;

    @Transactional
    public Map<String, Object> planExam(Long examId, List<Long> studentIds) {
        Exam exam = examService.getExamEntityById(examId);

        List<Student> students = studentIds.stream()
                .map(studentService::getStudentEntityById)
                .collect(Collectors.toList());

        for (Student student : students) {
            if (examAssignmentRepository.existsByExamAndStudent(exam, student)) {
                throw new DuplicateResourceException(
                        "Student " + student.getStudentNo() + " is already assigned to this exam");
            }
            if (examAssignmentRepository.existsByStudentAndExam_ExamDateAndExam_ExamTime(
                    student, exam.getExamDate(), exam.getExamTime())) {
                throw new DuplicateResourceException(
                        "Student " + student.getStudentNo() + " has a scheduling conflict at "
                                + exam.getExamDate() + " " + exam.getExamTime());
            }
        }

        List<Classroom> availableClassrooms = classroomRepository.findByIsAvailable(true)
                .stream()
                .filter(c -> !examRepository.existsByClassroomAndExamDateAndExamTime(
                        c, exam.getExamDate(), exam.getExamTime()))
                .sorted(Comparator.comparingInt(Classroom::getCapacity).reversed())
                .collect(Collectors.toList());

        if (availableClassrooms.isEmpty()) {
            throw new InsufficientCapacityException(
                    "No available classrooms found for " + exam.getExamDate() + " at " + exam.getExamTime());
        }

        int totalCapacity = availableClassrooms.stream().mapToInt(Classroom::getCapacity).sum();
        if (totalCapacity < students.size()) {
            throw new InsufficientCapacityException(
                    "Total available capacity (" + totalCapacity
                            + ") is insufficient for " + students.size() + " students");
        }

        students.sort(Comparator.comparing(Student::getStudentNo));

        List<Map<String, Object>> classroomSummaries = new ArrayList<>();
        Set<Long> assignedInstructorIds = new HashSet<>();
        int studentIndex = 0;
        int totalInvigilatorsAssigned = 0;

        for (Classroom classroom : availableClassrooms) {
            if (studentIndex >= students.size()) break;

            List<String> assignedStudentNos = new ArrayList<>();
            int seatNumber = 1;

            while (studentIndex < students.size() && seatNumber <= classroom.getCapacity()) {
                examAssignmentService.createExamAssignment(
                        exam, students.get(studentIndex), classroom, seatNumber);
                assignedStudentNos.add(students.get(studentIndex).getStudentNo());
                studentIndex++;
                seatNumber++;
            }

            int studentsInRoom = assignedStudentNos.size();
            int invigilatorsNeeded = calculateInvigilatorsNeeded(studentsInRoom);

            List<Instructor> roomInvigilators = assignInvigilators(
                    exam, classroom, invigilatorsNeeded, assignedInstructorIds);
            totalInvigilatorsAssigned += roomInvigilators.size();

            Map<String, Object> roomSummary = new LinkedHashMap<>();
            roomSummary.put("classroom", classroom.getCampus() + " - "
                    + classroom.getBuilding() + " - " + classroom.getRoomName());
            roomSummary.put("capacity", classroom.getCapacity());
            roomSummary.put("studentsAssigned", studentsInRoom);
            roomSummary.put("invigilatorsAssigned", roomInvigilators.size());
            roomSummary.put("studentNumbers", assignedStudentNos);
            roomSummary.put("invigilatorNames", roomInvigilators.stream()
                    .map(Instructor::getFullName)
                    .collect(Collectors.toList()));
            classroomSummaries.add(roomSummary);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("examId", exam.getExamId());
        result.put("examName", exam.getExamName());
        result.put("examDate", exam.getExamDate());
        result.put("examTime", exam.getExamTime());
        result.put("totalStudents", students.size());
        result.put("classroomsUsed", classroomSummaries.size());
        result.put("invigilatorsAssigned", totalInvigilatorsAssigned);
        result.put("classrooms", classroomSummaries);
        return result;
    }

    private int calculateInvigilatorsNeeded(int studentCount) {
        if (studentCount <= 50) return 1;
        if (studentCount <= 100) return 2;
        return 3;
    }

    private List<Instructor> assignInvigilators(Exam exam, Classroom classroom,
                                                 int count, Set<Long> assignedInstructorIds) {
        List<Instructor> candidates = instructorRepository.findAllByOrderByDutyCountAsc()
                .stream()
                .filter(Instructor::getIsAvailableForInvigilation)
                .filter(i -> !assignedInstructorIds.contains(i.getInstructorId()))
                .filter(i -> !invigilatorAssignmentRepository.existsByExamAndInstructor(exam, i))
                .filter(i -> !invigilatorAssignmentRepository.existsByInstructorAndExam_ExamDateAndExam_ExamTime(
                        i, exam.getExamDate(), exam.getExamTime()))
                .collect(Collectors.toList());

        if (candidates.size() < count) {
            throw new InsufficientCapacityException(
                    "Not enough available instructors for classroom " + classroom.getRoomName()
                            + ". Required: " + count + ", available: " + candidates.size());
        }

        List<Instructor> assigned = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Instructor instructor = candidates.get(i);
            invigilatorAssignmentService.createInvigilatorAssignment(exam, instructor, classroom);
            assignedInstructorIds.add(instructor.getInstructorId());
            assigned.add(instructor);
        }
        return assigned;
    }

    public Map<String, Object> autoScheduleExam(Long courseId, List<Long> studentIds, LocalDate preferredDate) {
        // TODO: Implement automatic exam scheduling
        // - Find available time slots across multiple days starting from preferredDate
        // - Check all potential classrooms and their availability
        // - Avoid conflicts for all provided students and available instructors
        // - Select optimal classroom combination minimising rooms used
        throw new UnsupportedOperationException("Auto-scheduling not yet implemented");
    }

    public List<Map<String, Object>> detectConflicts(Long examId) {
        // TODO: Implement conflict detection report
        // - Check for students double-booked at the same date and time
        // - Check for instructors invigilating multiple exams simultaneously
        // - Check for classrooms hosting multiple exams simultaneously
        // - Return structured list of all conflicts with full entity details
        throw new UnsupportedOperationException("Conflict detection not yet implemented");
    }

    public Map<String, Object> rebalanceInvigilators() {
        // TODO: Implement workload rebalancing
        // - Find instructors with duty counts above the group average
        // - Identify upcoming exams where reassignment is feasible
        // - Swap invigilators ensuring no new time conflicts are introduced
        // - Recalculate and persist updated duty counts for all affected instructors
        throw new UnsupportedOperationException("Rebalancing not yet implemented");
    }
}
