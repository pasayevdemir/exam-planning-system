package com.malik.examplanningsystem.repository;

import com.malik.examplanningsystem.entity.Department;
import com.malik.examplanningsystem.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByDepartmentName(String departmentName);

    List<Department> findByFaculty(Faculty faculty);
}
