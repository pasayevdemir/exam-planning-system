package com.malik.examplanningsystem.controller;

import com.malik.examplanningsystem.dto.ExamAssignmentCreateRequest;
import com.malik.examplanningsystem.dto.ExamAssignmentResponse;
import com.malik.examplanningsystem.service.ExamAssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/exam-assignments")
@AllArgsConstructor
@Tag(name = "Exam Assignments", description = "Assign students to exams and classrooms with seat numbers")
@SecurityRequirement(name = "Bearer Authentication")
public class ExamAssignmentController {

    private final ExamAssignmentService examAssignmentService;

    @PostMapping
    @Operation(summary = "Create an exam assignment", description = "Manually assigns a student to an exam in a specific classroom and seat")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Assignment created",
                    content = @Content(schema = @Schema(implementation = ExamAssignmentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Exam, student, or classroom not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Student already assigned to this exam", content = @Content)
    })
    public ResponseEntity<ExamAssignmentResponse> createAssignment(
            @Valid @RequestBody ExamAssignmentCreateRequest request) {
        return new ResponseEntity<>(examAssignmentService.createAssignment(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all exam assignments")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List returned",
                    content = @Content(schema = @Schema(implementation = ExamAssignmentResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public ResponseEntity<List<ExamAssignmentResponse>> getAllAssignments() {
        return ResponseEntity.ok(examAssignmentService.getAllAssignments());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get assignment by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Assignment found",
                    content = @Content(schema = @Schema(implementation = ExamAssignmentResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Assignment not found", content = @Content)
    })
    public ResponseEntity<ExamAssignmentResponse> getAssignmentById(@PathVariable Long id) {
        return ResponseEntity.ok(examAssignmentService.getAssignmentById(id));
    }

    @GetMapping("/exam/{examId}")
    @Operation(summary = "Get assignments by exam", description = "Returns all student-seat assignments for a given exam")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List returned",
                    content = @Content(schema = @Schema(implementation = ExamAssignmentResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Exam not found", content = @Content)
    })
    public ResponseEntity<List<ExamAssignmentResponse>> getAssignmentsByExam(@PathVariable Long examId) {
        return ResponseEntity.ok(examAssignmentService.getAssignmentsByExam(examId));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get assignments by student", description = "Returns all exams a student is assigned to")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List returned",
                    content = @Content(schema = @Schema(implementation = ExamAssignmentResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Student not found", content = @Content)
    })
    public ResponseEntity<List<ExamAssignmentResponse>> getAssignmentsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(examAssignmentService.getAssignmentsByStudent(studentId));
    }

    @GetMapping("/classroom/{classroomId}")
    @Operation(summary = "Get assignments by classroom")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List returned",
                    content = @Content(schema = @Schema(implementation = ExamAssignmentResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Classroom not found", content = @Content)
    })
    public ResponseEntity<List<ExamAssignmentResponse>> getAssignmentsByClassroom(@PathVariable Long classroomId) {
        return ResponseEntity.ok(examAssignmentService.getAssignmentsByClassroom(classroomId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an assignment")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Assignment deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Assignment not found", content = @Content)
    })
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        examAssignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }
}
