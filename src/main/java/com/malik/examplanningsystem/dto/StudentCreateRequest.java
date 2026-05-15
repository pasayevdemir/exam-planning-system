package com.malik.examplanningsystem.dto;

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
public class StudentCreateRequest {

    @NotBlank(message = "Student number cannot be blank")
    @Size(max = 20)
    private String studentNo;

    @Size(max = 11, message = "TC number must be 11 digits")
    private String tcNo;

    @NotBlank(message = "Full name cannot be blank")
    @Size(min = 3, max = 100)
    private String fullName;

    @NotNull(message = "Faculty ID cannot be null")
    private Long facultyId;

    @NotNull(message = "Department ID cannot be null")
    private Long departmentId;

    private Long userId;
}
