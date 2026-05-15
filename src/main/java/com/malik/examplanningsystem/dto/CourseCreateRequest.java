package com.malik.examplanningsystem.dto;

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
public class CourseCreateRequest {

    @NotBlank(message = "Course code is required")
    @Size(max = 20, message = "Course code must not exceed 20 characters")
    private String courseCode;

    @NotBlank(message = "Course name is required")
    @Size(min = 3, max = 150, message = "Course name must be between 3 and 150 characters")
    private String courseName;

    @NotNull(message = "Instructor ID is required")
    private Long instructorId;

    @NotNull(message = "Department ID is required")
    private Long departmentId;

    @Min(value = 1, message = "Credit hours must be at least 1")
    private Integer creditHours;

    @Size(max = 50, message = "Semester must not exceed 50 characters")
    private String semester;
}
