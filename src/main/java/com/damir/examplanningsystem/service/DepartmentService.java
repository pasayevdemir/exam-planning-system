package com.malik.examplanningsystem.service;

import com.malik.examplanningsystem.dto.DepartmentCreateRequest;
import com.malik.examplanningsystem.dto.DepartmentResponse;
import com.malik.examplanningsystem.entity.Department;
import com.malik.examplanningsystem.entity.Faculty;
import com.malik.examplanningsystem.exception.DuplicateResourceException;
import com.malik.examplanningsystem.exception.ResourceNotFoundException;
import com.malik.examplanningsystem.repository.DepartmentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final FacultyService facultyService;

    @Transactional
    public DepartmentResponse createDepartment(DepartmentCreateRequest request) {
        if(departmentRepository.findByDepartmentName(request.getDepartmentName()).isPresent()){
            throw new DuplicateResourceException("Department already exists");
        }

        Faculty faculty = facultyService.getFacultyEntityById(request.getFacultyId());

        Department department = new Department();
        department.setDepartmentName(request.getDepartmentName());
        department.setFaculty(faculty);

        Department savedDepartment = departmentRepository.save(department);
        return convertToResponse(savedDepartment);
    }

    @Transactional
    public DepartmentResponse updateDepartment(Long id, DepartmentCreateRequest request){
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        if(!department.getDepartmentName().equals(request.getDepartmentName()) &&
        departmentRepository.findByDepartmentName(request.getDepartmentName()).isPresent()){
            throw new DuplicateResourceException("Department already exists");
        }

        department.setDepartmentName(request.getDepartmentName());
        Faculty faculty = facultyService.getFacultyEntityById(request.getFacultyId());
        department.setFaculty(faculty);

        Department updatedDepartment = departmentRepository.save(department);
        return convertToResponse(updatedDepartment);
    }

    public List<DepartmentResponse> getAllDepartments(){
        return departmentRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    public List<DepartmentResponse> getDepartmentsByFacultyId(Long facultyId){
        Faculty faculty = facultyService.getFacultyEntityById(facultyId);
        return departmentRepository.findByFaculty(faculty)
                .stream()
                .map(this::convertToResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    public DepartmentResponse getDepartmentById(Long id){
        return convertToResponse(getDepartmentEntityById(id));
    }

    @Transactional
    public void deleteDepartment(Long id){
        if(!departmentRepository.existsById(id)){
            throw new ResourceNotFoundException("Department not found with id: " + id);
        }
        departmentRepository.deleteById(id);
    }

    private DepartmentResponse convertToResponse(Department department){
        return new DepartmentResponse(
                department.getDepartmentId(),
                department.getDepartmentName(),
                department.getFaculty().getFacultyId(),
                department.getFaculty().getFacultyName()
        );
    }

    public Department getDepartmentEntityById(Long id){
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
    }

}
