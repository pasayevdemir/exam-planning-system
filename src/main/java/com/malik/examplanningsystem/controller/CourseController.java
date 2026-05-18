package com.malik.examplanningsystem.controller;

import com.malik.examplanningsystem.dto.CourseCreateRequest;
import com.malik.examplanningsystem.dto.CourseResponse;
import com.malik.examplanningsystem.service.CourseService;
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
@RequestMapping("/api/admin/courses")
@Tag(name = "Courses", description = "Manage academic courses and their semester/instructor associations")
@SecurityRequirement(name = "Bearer Authentication")
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    @Operation(summary = "Create a course")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Course created",
                    content = @Content(schema = @Schema(implementation = CourseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "409", description = "Course code already exists", content = @Content)
    })
    public ResponseEntity<CourseResponse> createCourse(
            @Valid @RequestBody CourseCreateRequest request) {
        return new ResponseEntity<>(courseService.createCourse(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all courses")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List returned",
                    content = @Content(schema = @Schema(implementation = CourseResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Course found",
                    content = @Content(schema = @Schema(implementation = CourseResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Course not found", content = @Content)
    })
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping("/code/{courseCode}")
    @Operation(summary = "Get course by course code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Course found",
                    content = @Content(schema = @Schema(implementation = CourseResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Course not found", content = @Content)
    })
    public ResponseEntity<CourseResponse> getCourseByCourseCode(@PathVariable String courseCode) {
        return ResponseEntity.ok(courseService.getCourseByCourseCode(courseCode));
    }

    @GetMapping("/instructor/{instructorId}")
    @Operation(summary = "Get courses by instructor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List returned",
                    content = @Content(schema = @Schema(implementation = CourseResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Instructor not found", content = @Content)
    })
    public ResponseEntity<List<CourseResponse>> getCoursesByInstructor(
            @PathVariable Long instructorId) {
        return ResponseEntity.ok(courseService.getCoursesByInstructor(instructorId));
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Get courses by department")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List returned",
                    content = @Content(schema = @Schema(implementation = CourseResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Department not found", content = @Content)
    })
    public ResponseEntity<List<CourseResponse>> getCoursesByDepartment(
            @PathVariable Long departmentId) {
        return ResponseEntity.ok(courseService.getCoursesByDepartment(departmentId));
    }

    @GetMapping("/semester/{semester}")
    @Operation(summary = "Get courses by semester", description = "Example: FALL-2025 or SPRING-2026")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List returned",
                    content = @Content(schema = @Schema(implementation = CourseResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public ResponseEntity<List<CourseResponse>> getCoursesBySemester(@PathVariable String semester) {
        return ResponseEntity.ok(courseService.getCoursesBySemester(semester));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a course")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Course updated",
                    content = @Content(schema = @Schema(implementation = CourseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Course not found", content = @Content)
    })
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseCreateRequest request) {
        return ResponseEntity.ok(courseService.updateCourse(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a course")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Course deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Course not found", content = @Content)
    })
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
