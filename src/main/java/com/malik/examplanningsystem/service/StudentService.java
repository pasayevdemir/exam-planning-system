package com.malik.examplanningsystem.service;

import com.malik.examplanningsystem.dto.StudentCreateRequest;
import com.malik.examplanningsystem.dto.StudentResponse;
import com.malik.examplanningsystem.entity.Department;
import com.malik.examplanningsystem.entity.Faculty;
import com.malik.examplanningsystem.entity.Student;
import com.malik.examplanningsystem.entity.User;
import com.malik.examplanningsystem.exception.DuplicateResourceException;
import com.malik.examplanningsystem.exception.ResourceNotFoundException;
import com.malik.examplanningsystem.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final FacultyService facultyService;
    private final DepartmentService departmentService;
    private final UserService userService;

    @Transactional
    public StudentResponse createStudent(StudentCreateRequest request){
        if(studentRepository.existsByStudentNo(request.getStudentNo())){
            throw new DuplicateResourceException("Student with student number already exists");
        }

        if(request.getTcNo() != null && !request.getTcNo().isEmpty()){
            if(studentRepository.existsByTcNo(request.getTcNo())){
                throw new DuplicateResourceException("Student with tc number already exists");
            }
        }

        Faculty faculty = facultyService.getFacultyEntityById(request.getFacultyId());
        Department department = departmentService.getDepartmentEntityById(request.getDepartmentId());

        Student student = new Student();
        student.setStudentNo(request.getStudentNo());
        student.setTcNo(request.getTcNo());
        student.setFullName(request.getFullName());
        student.setDepartment(department);
        student.setFaculty(faculty);

        if(request.getUserId() != null){
            User user = userService.getUserById(request.getUserId());
            student.setUser(user);
        }

        Student savedStudent = studentRepository.save(student);
        return convertToResponse(savedStudent);
    }

    public List<StudentResponse> getAllStudents(){
        return studentRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public StudentResponse getStudentById(Long id){
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return convertToResponse(student);
    }

    public StudentResponse getStudentByStudentNo(String studentNo){
        Student student = studentRepository.findByStudentNo(studentNo)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with student number: " + studentNo));

        return convertToResponse(student);
    }

    public List<StudentResponse> getStudentsByDepartment(Long departmentId){
        Department department = departmentService.getDepartmentEntityById(departmentId);
        return studentRepository.findByDepartment(department)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<StudentResponse> getStudentsByFaculty(Long facultyId){
        Faculty faculty = facultyService.getFacultyEntityById(facultyId);
        return studentRepository.findByFaculty(faculty)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public StudentResponse updateStudent(Long id, StudentCreateRequest request){
        Student student = getStudentEntityById(id);

        if(!student.getStudentNo().equals(request.getStudentNo())
                && studentRepository.existsByStudentNo(request.getStudentNo())){
            throw new DuplicateResourceException("Student with student number already exists");
        }

        if(request.getTcNo() != null && !request.getTcNo().isEmpty()){
            if(!student.getTcNo().equals(request.getTcNo())
                    && studentRepository.existsByTcNo(request.getTcNo())){
                throw new DuplicateResourceException("Student with tc number already exists");
            }
        }

        student.setStudentNo(request.getStudentNo());
        student.setTcNo(request.getTcNo());
        student.setFullName(request.getFullName());

        Faculty faculty = facultyService.getFacultyEntityById(request.getFacultyId());
        Department department = departmentService.getDepartmentEntityById(request.getDepartmentId());

        student.setFaculty(faculty);
        student.setDepartment(department);

        return convertToResponse(studentRepository.save(student));
    }

    @Transactional
    public void deleteStudent(Long id){
        if(!studentRepository.existsById(id)){
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }

    public Student getStudentEntityById(Long id){
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    private StudentResponse convertToResponse(Student student){
        return new StudentResponse(
                student.getStudentId(),
                student.getStudentNo(),
                student.getTcNo(),
                student.getFullName(),
                student.getFaculty().getFacultyId(),
                student.getFaculty().getFacultyName(),
                student.getDepartment().getDepartmentId(),
                student.getDepartment().getDepartmentName(),
                student.getCreatedAt()
        );
    }
}
