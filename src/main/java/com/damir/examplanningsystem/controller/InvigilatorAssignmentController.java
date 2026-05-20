package com.malik.examplanningsystem.controller;

import com.malik.examplanningsystem.dto.InvigilatorAssignmentCreateRequest;
import com.malik.examplanningsystem.dto.InvigilatorAssignmentResponse;
import com.malik.examplanningsystem.service.InvigilatorAssignmentService;
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
@RequestMapping("/api/admin/invigilator-assignments")
@AllArgsConstructor
@Tag(name = "Invigilator Assignments", description = "Assign instructors as invigilators to exams and classrooms")
@SecurityRequirement(name = "Bearer Authentication")
public class InvigilatorAssignmentController {

    private final InvigilatorAssignmentService invigilatorAssignmentService;

    @PostMapping
    @Operation(summary = "Create an invigilator assignment", description = "Manually assigns an instructor as invigilator for an exam in a classroom")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Assignment created",
                    content = @Content(schema = @Schema(implementation = InvigilatorAssignmentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Exam, instructor, or classroom not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Instructor already assigned to this exam", content = @Content)
    })
    public ResponseEntity<InvigilatorAssignmentResponse> createAssignment(
            @Valid @RequestBody InvigilatorAssignmentCreateRequest request) {
        return new ResponseEntity<>(invigilatorAssignmentService.createAssignment(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all invigilator assignments")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List returned",
                    content = @Content(schema = @Schema(implementation = InvigilatorAssignmentResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public ResponseEntity<List<InvigilatorAssignmentResponse>> getAllAssignments() {
        return ResponseEntity.ok(invigilatorAssignmentService.getAllAssignments());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get invigilator assignment by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Assignment found",
                    content = @Content(schema = @Schema(implementation = InvigilatorAssignmentResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Assignment not found", content = @Content)
    })
    public ResponseEntity<InvigilatorAssignmentResponse> getAssignmentById(@PathVariable Long id) {
        return ResponseEntity.ok(invigilatorAssignmentService.getAssignmentById(id));
    }

    @GetMapping("/exam/{examId}")
    @Operation(summary = "Get invigilator assignments by exam")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List returned",
                    content = @Content(schema = @Schema(implementation = InvigilatorAssignmentResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Exam not found", content = @Content)
    })
    public ResponseEntity<List<InvigilatorAssignmentResponse>> getAssignmentsByExam(@PathVariable Long examId) {
        return ResponseEntity.ok(invigilatorAssignmentService.getAssignmentsByExam(examId));
    }

    @GetMapping("/instructor/{instructorId}")
    @Operation(summary = "Get invigilator assignments by instructor", description = "Returns all exams an instructor is assigned to invigilate")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List returned",
                    content = @Content(schema = @Schema(implementation = InvigilatorAssignmentResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Instructor not found", content = @Content)
    })
    public ResponseEntity<List<InvigilatorAssignmentResponse>> getAssignmentsByInstructor(
            @PathVariable Long instructorId) {
        return ResponseEntity.ok(invigilatorAssignmentService.getAssignmentsByInstructor(instructorId));
    }

    @GetMapping("/classroom/{classroomId}")
    @Operation(summary = "Get invigilator assignments by classroom")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List returned",
                    content = @Content(schema = @Schema(implementation = InvigilatorAssignmentResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Classroom not found", content = @Content)
    })
    public ResponseEntity<List<InvigilatorAssignmentResponse>> getAssignmentsByClassroom(
            @PathVariable Long classroomId) {
        return ResponseEntity.ok(invigilatorAssignmentService.getAssignmentsByClassroom(classroomId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an invigilator assignment")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Assignment deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Assignment not found", content = @Content)
    })
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        invigilatorAssignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }
}
