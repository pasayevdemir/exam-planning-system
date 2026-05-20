package com.malik.examplanningsystem.controller;

import com.malik.examplanningsystem.dto.ClassroomCreateRequest;
import com.malik.examplanningsystem.dto.ClassroomResponse;
import com.malik.examplanningsystem.service.ClassroomService;
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
@AllArgsConstructor
@RequestMapping("/api/admin/classrooms")
@Tag(name = "Classrooms", description = "Manage exam venues and their capacities")
@SecurityRequirement(name = "Bearer Authentication")
public class ClassroomController {

    private final ClassroomService classroomService;

    @PostMapping
    @Operation(summary = "Create a classroom")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Classroom created",
                    content = @Content(schema = @Schema(implementation = ClassroomResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "409", description = "Room number already exists", content = @Content)
    })
    public ResponseEntity<ClassroomResponse> createClassroom(
            @Valid @RequestBody ClassroomCreateRequest request){
        ClassroomResponse response = classroomService.createClassroom(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all classrooms")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List returned",
                    content = @Content(schema = @Schema(implementation = ClassroomResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public ResponseEntity<List<ClassroomResponse>> getAllClassrooms(){
        return ResponseEntity.ok(classroomService.getAllClassrooms());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get classroom by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Classroom found",
                    content = @Content(schema = @Schema(implementation = ClassroomResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Classroom not found", content = @Content)
    })
    public ResponseEntity<ClassroomResponse> getClassroomById(@PathVariable Long id){
        return ResponseEntity.ok(classroomService.getClassroomById(id));
    }

    @GetMapping("/available")
    @Operation(summary = "Get available classrooms",
            description = "Returns classrooms marked as available with at least the given minimum capacity")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Available classrooms returned",
                    content = @Content(schema = @Schema(implementation = ClassroomResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public ResponseEntity<List<ClassroomResponse>> getAvailableClassrooms(
            @RequestParam(required = false, defaultValue = "1") Integer minCapacity){
        return ResponseEntity.ok(classroomService.getAvailableClassrooms(minCapacity));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a classroom")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Classroom updated",
                    content = @Content(schema = @Schema(implementation = ClassroomResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Classroom not found", content = @Content)
    })
    public ResponseEntity<ClassroomResponse> updateClassroom(
            @PathVariable Long id,
            @Valid @RequestBody ClassroomCreateRequest request){
        return ResponseEntity.ok(classroomService.updateClassroom(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a classroom")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Classroom deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Classroom not found", content = @Content)
    })
    public ResponseEntity<Void> deleteClassroom(@PathVariable Long id){
        classroomService.deleteClassroom(id);
        return ResponseEntity.noContent().build();
    }
}
