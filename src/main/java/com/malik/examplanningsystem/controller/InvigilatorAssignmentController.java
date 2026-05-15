package com.malik.examplanningsystem.controller;

import com.malik.examplanningsystem.dto.InvigilatorAssignmentCreateRequest;
import com.malik.examplanningsystem.dto.InvigilatorAssignmentResponse;
import com.malik.examplanningsystem.service.InvigilatorAssignmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/invigilator-assignments")
@AllArgsConstructor
public class InvigilatorAssignmentController {

    private final InvigilatorAssignmentService invigilatorAssignmentService;

    @PostMapping
    public ResponseEntity<InvigilatorAssignmentResponse> createAssignment(
            @Valid @RequestBody InvigilatorAssignmentCreateRequest request) {
        return new ResponseEntity<>(invigilatorAssignmentService.createAssignment(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<InvigilatorAssignmentResponse>> getAllAssignments() {
        return ResponseEntity.ok(invigilatorAssignmentService.getAllAssignments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvigilatorAssignmentResponse> getAssignmentById(@PathVariable Long id) {
        return ResponseEntity.ok(invigilatorAssignmentService.getAssignmentById(id));
    }

    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<InvigilatorAssignmentResponse>> getAssignmentsByExam(@PathVariable Long examId) {
        return ResponseEntity.ok(invigilatorAssignmentService.getAssignmentsByExam(examId));
    }

    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<InvigilatorAssignmentResponse>> getAssignmentsByInstructor(
            @PathVariable Long instructorId) {
        return ResponseEntity.ok(invigilatorAssignmentService.getAssignmentsByInstructor(instructorId));
    }

    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<List<InvigilatorAssignmentResponse>> getAssignmentsByClassroom(
            @PathVariable Long classroomId) {
        return ResponseEntity.ok(invigilatorAssignmentService.getAssignmentsByClassroom(classroomId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        invigilatorAssignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }
}
