package com.malik.examplanningsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for creating or updating a course")
public class CourseCreateRequest {

    @NotBlank(message = "Course code is required")
    @Size(max = 20, message = "Course code must not exceed 20 characters")
    @Schema(description = "Unique course code", example = "CS301")
    private String courseCode;

    @NotBlank(message = "Course name is required")
    @Size(min = 3, max = 150, message = "Course name must be between 3 and 150 characters")
    @Schema(description = "Full name of the course", example = "Data Structures and Algorithms")
    private String courseName;

    @NotNull(message = "Instructor ID is required")
    @Schema(description = "ID of the instructor responsible for this course", example = "3")
    private Long instructorId;

    @NotNull(message = "Department ID is required")
    @Schema(description = "ID of the department offering this course", example = "2")
    private Long departmentId;

    @Min(value = 1, message = "Credit hours must be at least 1")
    @Schema(description = "Number of credit hours", example = "3")
    private Integer creditHours;

    @Size(max = 50, message = "Semester must not exceed 50 characters")
    @Schema(description = "Semester code", example = "FALL-2025")
    private String semester;
}
