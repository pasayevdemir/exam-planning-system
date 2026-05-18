package com.malik.examplanningsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Request body for creating or updating a student")
public class StudentCreateRequest {

    @NotBlank(message = "Student number cannot be blank")
    @Size(max = 20)
    @Schema(description = "Unique student registration number", example = "STU-2024-001")
    private String studentNo;

    @Size(max = 11, message = "TC number must be 11 digits")
    @Schema(description = "Turkish citizen ID number (11 digits)", example = "12345678901")
    private String tcNo;

    @NotBlank(message = "Full name cannot be blank")
    @Size(min = 3, max = 100)
    @Schema(description = "Full name of the student", example = "Malik Salimov")
    private String fullName;

    @NotNull(message = "Faculty ID cannot be null")
    @Schema(description = "ID of the faculty the student is enrolled in", example = "1")
    private Long facultyId;

    @NotNull(message = "Department ID cannot be null")
    @Schema(description = "ID of the department the student belongs to", example = "2")
    private Long departmentId;

    @Schema(description = "Optional linked user account ID", example = "7")
    private Long userId;
}
