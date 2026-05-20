package com.malik.examplanningsystem.repository;

import com.malik.examplanningsystem.entity.Course;
import com.malik.examplanningsystem.entity.Department;
import com.malik.examplanningsystem.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCourseCode(String courseCode);
    List<Course> findByInstructor(Instructor instructor);
    List<Course> findByDepartment(Department department);
    List<Course> findBySemester(String semester);
    boolean existsByCourseCode(String courseCode);
}
