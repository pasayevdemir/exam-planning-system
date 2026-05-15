package com.malik.examplanningsystem.controller;

import com.malik.examplanningsystem.dto.ExamAssignmentCreateRequest;
import com.malik.examplanningsystem.dto.ExamAssignmentResponse;
import com.malik.examplanningsystem.service.ExamAssignmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/exam-assignments")
@AllArgsConstructor
public class ExamAssignmentController {

    private final ExamAssignmentService examAssignmentService;

    @PostMapping
    public ResponseEntity<ExamAssignmentResponse> createAssignment(
            @Valid @RequestBody ExamAssignmentCreateRequest request) {
        return new ResponseEntity<>(examAssignmentService.createAssignment(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ExamAssignmentResponse>> getAllAssignments() {
        return ResponseEntity.ok(examAssignmentService.getAllAssignments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamAssignmentResponse> getAssignmentById(@PathVariable Long id) {
        return ResponseEntity.ok(examAssignmentService.getAssignmentById(id));
    }

    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<ExamAssignmentResponse>> getAssignmentsByExam(@PathVariable Long examId) {
        return ResponseEntity.ok(examAssignmentService.getAssignmentsByExam(examId));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ExamAssignmentResponse>> getAssignmentsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(examAssignmentService.getAssignmentsByStudent(studentId));
    }

    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<List<ExamAssignmentResponse>> getAssignmentsByClassroom(@PathVariable Long classroomId) {
        return ResponseEntity.ok(examAssignmentService.getAssignmentsByClassroom(classroomId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        examAssignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }
}
