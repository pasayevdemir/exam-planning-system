package com.malik.examplanningsystem.service;

import com.malik.examplanningsystem.dto.ExamCreateRequest;
import com.malik.examplanningsystem.dto.ExamResponse;
import com.malik.examplanningsystem.entity.Classroom;
import com.malik.examplanningsystem.entity.Course;
import com.malik.examplanningsystem.entity.Exam;
import com.malik.examplanningsystem.exception.DuplicateResourceException;
import com.malik.examplanningsystem.exception.ResourceNotFoundException;
import com.malik.examplanningsystem.repository.ExamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;
    private final CourseService courseService;
    private final ClassroomService classroomService;

    @Transactional
    public ExamResponse createExam(ExamCreateRequest request) {
        Course course = courseService.getCourseEntityById(request.getCourseId());
        Classroom classroom = classroomService.getClassroomEntityById(request.getClassroomId());

        if (examRepository.existsByClassroomAndExamDateAndExamTime(classroom, request.getExamDate(), request.getExamTime())) {
            throw new DuplicateResourceException("Classroom is already booked for this date and time");
        }

        Exam exam = new Exam();
        exam.setExamName(request.getExamName());
        exam.setExamType(request.getExamType());
        exam.setExamDate(request.getExamDate());
        exam.setExamTime(request.getExamTime());
        exam.setDuration(request.getDuration());
        exam.setCourse(course);
        exam.setClassroom(classroom);
        exam.setIsCommonExam(request.getIsCommonExam() != null ? request.getIsCommonExam() : false);

        return convertToResponse(examRepository.save(exam));
    }

    public List<ExamResponse> getAllExams() {
        return examRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ExamResponse getExamById(Long id) {
        return convertToResponse(getExamEntityById(id));
    }

    public List<ExamResponse> getExamsByCourse(Long courseId) {
        Course course = courseService.getCourseEntityById(courseId);
        return examRepository.findByCourse(course).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ExamResponse> getExamsByClassroom(Long classroomId) {
        Classroom classroom = classroomService.getClassroomEntityById(classroomId);
        return examRepository.findByClassroom(classroom).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ExamResponse> getExamsByDate(LocalDate date) {
        return examRepository.findByExamDate(date).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ExamResponse updateExam(Long id, ExamCreateRequest request) {
        Exam exam = getExamEntityById(id);
        Course course = courseService.getCourseEntityById(request.getCourseId());
        Classroom classroom = classroomService.getClassroomEntityById(request.getClassroomId());

        boolean classroomChanged = !exam.getClassroom().getClassroomId().equals(request.getClassroomId());
        boolean dateChanged = !exam.getExamDate().equals(request.getExamDate());
        boolean timeChanged = !exam.getExamTime().equals(request.getExamTime());

        if ((classroomChanged || dateChanged || timeChanged) &&
                examRepository.existsByClassroomAndExamDateAndExamTime(classroom, request.getExamDate(), request.getExamTime())) {
            throw new DuplicateResourceException("Classroom is already booked for this date and time");
        }

        exam.setExamName(request.getExamName());
        exam.setExamType(request.getExamType());
        exam.setExamDate(request.getExamDate());
        exam.setExamTime(request.getExamTime());
        exam.setDuration(request.getDuration());
        exam.setCourse(course);
        exam.setClassroom(classroom);
        exam.setIsCommonExam(request.getIsCommonExam() != null ? request.getIsCommonExam() : false);

        return convertToResponse(examRepository.save(exam));
    }

    @Transactional
    public void deleteExam(Long id) {
        if (!examRepository.existsById(id)) {
            throw new ResourceNotFoundException("Exam not found with id: " + id);
        }
        examRepository.deleteById(id);
    }

    public Exam getExamEntityById(Long id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with id: " + id));
    }

    private ExamResponse convertToResponse(Exam exam) {
        ExamResponse response = new ExamResponse();
        response.setExamId(exam.getExamId());
        response.setExamName(exam.getExamName());
        response.setExamType(exam.getExamType());
        response.setExamDate(exam.getExamDate());
        response.setExamTime(exam.getExamTime());
        response.setDuration(exam.getDuration());
        response.setCourseId(exam.getCourse().getCourseId());
        response.setCourseName(exam.getCourse().getCourseName());
        response.setCourseCode(exam.getCourse().getCourseCode());
        response.setClassroomId(exam.getClassroom().getClassroomId());
        response.setClassroomName(exam.getClassroom().getCampus() + " - " +
                exam.getClassroom().getBuilding() + " - " +
                exam.getClassroom().getRoomName());
        response.setClassroomCapacity(exam.getClassroom().getCapacity());
        response.setIsCommonExam(exam.getIsCommonExam());
        response.setCreatedAt(exam.getCreatedAt());
        return response;
    }
}
