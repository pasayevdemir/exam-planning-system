package com.malik.examplanningsystem.dto;

import com.malik.examplanningsystem.entity.Role;
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
public class RegisterRequest {

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6)
    private String password;

    @NotNull(message = "Role cannot be null")
    private Role role;
}
