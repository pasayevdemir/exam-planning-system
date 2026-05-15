package com.malik.examplanningsystem.dto;

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
public class FacultyCreateRequest {
    @NotBlank(message = "Faculty name cannot be blank")
    @Size(min = 3, max = 100, message = "Faculty name must be between 3 and 100 characters")
    private String facultyName;
}
