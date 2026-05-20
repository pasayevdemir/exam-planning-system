package com.malik.examplanningsystem.service;

import com.malik.examplanningsystem.dto.CourseCreateRequest;
import com.malik.examplanningsystem.dto.CourseResponse;
import com.malik.examplanningsystem.entity.Course;
import com.malik.examplanningsystem.entity.Department;
import com.malik.examplanningsystem.entity.Instructor;
import com.malik.examplanningsystem.exception.DuplicateResourceException;
import com.malik.examplanningsystem.exception.ResourceNotFoundException;
import com.malik.examplanningsystem.repository.CourseRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final InstructorService instructorService;
    private final DepartmentService departmentService;

    @Transactional
    public CourseResponse createCourse(CourseCreateRequest request) {
        if (courseRepository.existsByCourseCode(request.getCourseCode())) {
            throw new DuplicateResourceException("Course with code '" + request.getCourseCode() + "' already exists");
        }

        Instructor instructor = instructorService.getInstructorEntityById(request.getInstructorId());
        Department department = departmentService.getDepartmentEntityById(request.getDepartmentId());

        Course course = new Course();
        course.setCourseCode(request.getCourseCode());
        course.setCourseName(request.getCourseName());
        course.setInstructor(instructor);
        course.setDepartment(department);
        course.setCreditHours(request.getCreditHours());
        course.setSemester(request.getSemester());

        return convertToResponse(courseRepository.save(course));
    }

    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public CourseResponse getCourseById(Long id) {
        return convertToResponse(getCourseEntityById(id));
    }

    public CourseResponse getCourseByCourseCode(String courseCode) {
        return convertToResponse(
                courseRepository.findByCourseCode(courseCode)
                        .orElseThrow(() -> new ResourceNotFoundException("Course not found with code: " + courseCode))
        );
    }

    public List<CourseResponse> getCoursesByInstructor(Long instructorId) {
        Instructor instructor = instructorService.getInstructorEntityById(instructorId);
        return courseRepository.findByInstructor(instructor)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<CourseResponse> getCoursesByDepartment(Long departmentId) {
        Department department = departmentService.getDepartmentEntityById(departmentId);
        return courseRepository.findByDepartment(department)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<CourseResponse> getCoursesBySemester(String semester) {
        return courseRepository.findBySemester(semester)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CourseResponse updateCourse(Long id, CourseCreateRequest request) {
        Course course = getCourseEntityById(id);

        if (!course.getCourseCode().equals(request.getCourseCode()) &&
                courseRepository.existsByCourseCode(request.getCourseCode())) {
            throw new DuplicateResourceException("Course with code '" + request.getCourseCode() + "' already exists");
        }

        Instructor instructor = instructorService.getInstructorEntityById(request.getInstructorId());
        Department department = departmentService.getDepartmentEntityById(request.getDepartmentId());

        course.setCourseCode(request.getCourseCode());
        course.setCourseName(request.getCourseName());
        course.setInstructor(instructor);
        course.setDepartment(department);
        course.setCreditHours(request.getCreditHours());
        course.setSemester(request.getSemester());

        return convertToResponse(courseRepository.save(course));
    }

    @Transactional
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }

    public Course getCourseEntityById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    private CourseResponse convertToResponse(Course course) {
        CourseResponse response = new CourseResponse();
        response.setCourseId(course.getCourseId());
        response.setCourseCode(course.getCourseCode());
        response.setCourseName(course.getCourseName());
        response.setInstructorId(course.getInstructor().getInstructorId());
        response.setInstructorName(course.getInstructor().getFullName());
        response.setDepartmentId(course.getDepartment().getDepartmentId());
        response.setDepartmentName(course.getDepartment().getDepartmentName());
        response.setCreditHours(course.getCreditHours());
        response.setSemester(course.getSemester());
        response.setCreatedAt(course.getCreatedAt());
        return response;
    }
}
