package com.malik.examplanningsystem.service;

import com.malik.examplanningsystem.entity.Department;
import com.malik.examplanningsystem.entity.Faculty;
import com.malik.examplanningsystem.exception.DuplicateResourceException;
import com.malik.examplanningsystem.exception.ResourceNotFoundException;
import com.malik.examplanningsystem.repository.DepartmentRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final FacultyService facultyService;

    @Transactional
    public Department createDepartment(String departmentName, Long facultyId) {
        if(departmentRepository.findByDepartmentName(departmentName).isPresent()){
            throw new DuplicateResourceException("Department with name '" + departmentName + "' already exists");
        }
        Faculty faculty = facultyService.getFacultyEntityById(facultyId);
        Department department = new Department();
        department.setDepartmentName(departmentName);
        department.setFaculty(faculty);
        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartments(){
        return departmentRepository.findAll();
    }

    public List<Department> getDepartmentsByFacultyId(Long facultyId){
        Faculty faculty = facultyService.getFacultyEntityById(facultyId);
        return departmentRepository.findByFaculty(faculty);
    }

    public Department getDepartmentById(Long id){
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
    }

    @Transactional
    public void deleteDepartment(Long id){
        if(!departmentRepository.existsById(id)){
            throw new ResourceNotFoundException("Department not found with id: " + id);
        }
        departmentRepository.deleteById(id);
    }

}
