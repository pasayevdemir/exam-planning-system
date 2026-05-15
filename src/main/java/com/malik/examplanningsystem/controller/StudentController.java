package com.malik.examplanningsystem.controller;

import com.malik.examplanningsystem.dto.StudentCreateRequest;
import com.malik.examplanningsystem.dto.StudentResponse;
import com.malik.examplanningsystem.service.StudentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/students")
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(
            @Valid @RequestBody StudentCreateRequest request){
        StudentResponse response = studentService.createStudent(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents(){
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id){
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping("/number/{studentNo}")
    public ResponseEntity<StudentResponse> getStudentByStudentNo(@PathVariable String studentNo){
        return ResponseEntity.ok(studentService.getStudentByStudentNo(studentNo));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<StudentResponse>> getStudentsByDepartment(
            @PathVariable Long departmentId
    ){
        return ResponseEntity.ok(studentService.getStudentsByDepartment(departmentId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentCreateRequest request
    ){
        return ResponseEntity.ok(studentService.updateStudent(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id){
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
