package com.malik.examplanningsystem.repository;

import com.malik.examplanningsystem.entity.Classroom;
import com.malik.examplanningsystem.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    List<Classroom> findByCampus(String campus);
    List<Classroom> findByCapacityGreaterThanEqual(Integer capacity);
    List<Classroom> findByIsAvailable(Boolean isAvailable);
}
