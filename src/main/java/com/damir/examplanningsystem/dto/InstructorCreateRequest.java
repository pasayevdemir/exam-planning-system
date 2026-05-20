package com.malik.examplanningsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
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
@Schema(description = "Request body for creating or updating an instructor")
public class InstructorCreateRequest {

    @NotBlank(message = "Staff number cannot be blank")
    @Size(max = 20)
    @Schema(description = "Unique staff number", example = "STAFF-001")
    private String staffNo;

    @NotBlank(message = "Full name cannot be blank")
    @Size(min = 3, max = 100)
    @Schema(description = "Full name of the instructor", example = "Dr. Ali Karimov")
    private String fullName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @Schema(description = "Institutional email address", example = "a.karimov@university.edu")
    private String email;

    @NotNull(message = "Department ID cannot be null")
    @Schema(description = "ID of the department the instructor belongs to", example = "2")
    private Long departmentId;

    @Schema(description = "Whether this instructor is available as an invigilator", example = "true")
    private Boolean isAvailableForInvigilation = true;

    @Schema(description = "Optional linked user account ID", example = "5")
    private Long userId;
}
