package com.malik.examplanningsystem.dto;

import com.malik.examplanningsystem.entity.Role;
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
@Schema(description = "Registration request for a new user account")
public class RegisterRequest {

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50)
    @Schema(description = "Unique username (3–50 characters)", example = "malik_admin")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6)
    @Schema(description = "Password (minimum 6 characters)", example = "secret123")
    private String password;

    @NotNull(message = "Role cannot be null")
    @Schema(description = "Role assigned to the user", example = "ADMIN")
    private Role role;
}
