package com.malik.examplanningsystem.dto;

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
public class InstructorCreateRequest {

    @NotBlank(message = "Staff number cannot be blank")
    @Size(max = 20)
    private String staffNo;

    @NotBlank(message = "Full name cannot be blank")
    @Size(min = 3, max = 100)
    private String fullName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Department ID cannot be null")
    private Long departmentId;

    private Boolean isAvailableForInvigilation = true;

    private Long userId;
}
