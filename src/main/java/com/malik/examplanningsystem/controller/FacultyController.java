package com.malik.examplanningsystem.controller;

import com.malik.examplanningsystem.dto.FacultyCreateRequest;
import com.malik.examplanningsystem.dto.FacultyResponse;
import com.malik.examplanningsystem.service.FacultyService;
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
@RequestMapping("/api/admin/faculties")
@Tag(name = "Faculties", description = "Manage university faculties")
@SecurityRequirement(name = "Bearer Authentication")
public class FacultyController {

    private final FacultyService facultyService;

    @PostMapping
    @Operation(summary = "Create a faculty", description = "Creates a new faculty. Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Faculty created",
                    content = @Content(schema = @Schema(implementation = FacultyResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "409", description = "Faculty name already exists", content = @Content)
    })
    public ResponseEntity<FacultyResponse> createFaculty(
            @Valid @RequestBody FacultyCreateRequest facultyCreateRequest) {
        FacultyResponse facultyResponse = facultyService.createFaculty(facultyCreateRequest);
        return new ResponseEntity<>(facultyResponse, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all faculties", description = "Returns a list of all faculties")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List returned successfully",
                    content = @Content(schema = @Schema(implementation = FacultyResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public ResponseEntity<List<FacultyResponse>> getAllFaculties() {
        List<FacultyResponse> facultyResponses = facultyService.getAllFaculties();
        return ResponseEntity.ok(facultyResponses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get faculty by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Faculty found",
                    content = @Content(schema = @Schema(implementation = FacultyResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Faculty not found", content = @Content)
    })
    public ResponseEntity<FacultyResponse> getFacultyById(@PathVariable Long id) {
        FacultyResponse facultyResponse = facultyService.getFacultyById(id);
        return ResponseEntity.ok(facultyResponse);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a faculty")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Faculty updated",
                    content = @Content(schema = @Schema(implementation = FacultyResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Faculty not found", content = @Content)
    })
    public ResponseEntity<FacultyResponse> updateFaculty(
            @PathVariable Long id,
            @Valid @RequestBody FacultyCreateRequest facultyCreateRequest
    ){
        FacultyResponse updated = facultyService.updateFaculty(id, facultyCreateRequest);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a faculty")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Faculty deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Faculty not found", content = @Content)
    })
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id){
        facultyService.deleteFaculty(id);
        return ResponseEntity.noContent().build();
    }
}
