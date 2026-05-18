package com.malik.examplanningsystem.dto;

import com.malik.examplanningsystem.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "JWT authentication response")
public class AuthResponse {

    @Schema(description = "JWT Bearer token to include in Authorization header", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;

    @Schema(description = "Username of the authenticated user", example = "malik_admin")
    private String username;

    @Schema(description = "Role of the authenticated user", example = "ADMIN")
    private Role role;

    @Schema(description = "Token validity in milliseconds", example = "86400000")
    private Long expiresIn;
}
