package com.malik.examplanningsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for creating or updating a faculty")
public class FacultyCreateRequest {

    @NotBlank(message = "Faculty name cannot be blank")
    @Size(min = 3, max = 100, message = "Faculty name must be between 3 and 100 characters")
    @Schema(description = "Name of the faculty", example = "Faculty of Engineering")
    private String facultyName;
}
