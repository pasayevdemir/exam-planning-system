package com.malik.examplanningsystem.controller;

import com.malik.examplanningsystem.dto.AuthResponse;
import com.malik.examplanningsystem.dto.LoginRequest;
import com.malik.examplanningsystem.dto.RegisterRequest;
import com.malik.examplanningsystem.entity.User;
import com.malik.examplanningsystem.security.CustomUserDetailsService;
import com.malik.examplanningsystem.security.JwtService;
import com.malik.examplanningsystem.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    public AuthController(UserService userService,
                          JwtService jwtService,
                          CustomUserDetailsService userDetailsService,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest){
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPasswordHash(registerRequest.getPassword());
        user.setRole(registerRequest.getRole());

        User createdUser = userService.createUser(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(createdUser.getUsername());
        String token = jwtService.generateToken(userDetails);

        AuthResponse authResponse = new AuthResponse(
                token,
                createdUser.getUsername(),
                createdUser.getRole(),
                jwtExpiration
        );

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        String token = jwtService.generateToken(userDetails);

        User user = userService.getUserByUsername(loginRequest.getUsername());

        AuthResponse authResponse = new AuthResponse(
                token,
                user.getUsername(),
                user.getRole(),
                jwtExpiration
        );

        return ResponseEntity.ok(authResponse);
    }
}
