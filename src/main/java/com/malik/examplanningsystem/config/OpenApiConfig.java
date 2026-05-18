package com.malik.examplanningsystem.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Exam Planning System API")
                        .version("1.0")
                        .description("REST API for managing university exam scheduling, student assignments, and invigilator coordination.")
                        .contact(new Contact()
                                .name("Malik Salimov")
                                .email("nizamitaghizadaa@gmail.com")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .name("Bearer Authentication")
                                .description("Provide your JWT token. Obtain it from POST /api/auth/login")));
    }

    @Bean
    public GroupedOpenApi authGroup() {
        return GroupedOpenApi.builder()
                .group("1-authentication")
                .displayName("Authentication")
                .pathsToMatch("/api/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi organizationGroup() {
        return GroupedOpenApi.builder()
                .group("2-organization")
                .displayName("Organization (Faculties & Departments)")
                .pathsToMatch("/api/admin/faculties/**", "/api/admin/departments/**")
                .build();
    }

    @Bean
    public GroupedOpenApi peopleGroup() {
        return GroupedOpenApi.builder()
                .group("3-people")
                .displayName("People (Students, Instructors, Users)")
                .pathsToMatch("/api/admin/students/**", "/api/admin/instructors/**", "/api/admin/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi academicsGroup() {
        return GroupedOpenApi.builder()
                .group("4-academics")
                .displayName("Academics (Courses & Classrooms)")
                .pathsToMatch("/api/admin/courses/**", "/api/admin/classrooms/**")
                .build();
    }

    @Bean
    public GroupedOpenApi examsGroup() {
        return GroupedOpenApi.builder()
                .group("5-exams")
                .displayName("Exams & Assignments")
                .pathsToMatch("/api/admin/exams/**", "/api/admin/exam-assignments/**", "/api/admin/invigilator-assignments/**")
                .build();
    }

    @Bean
    public GroupedOpenApi planningGroup() {
        return GroupedOpenApi.builder()
                .group("6-planning")
                .displayName("Exam Planning (Algorithm)")
                .pathsToMatch("/api/admin/exam-planning/**")
                .build();
    }

    @Bean
    public GroupedOpenApi allGroup() {
        return GroupedOpenApi.builder()
                .group("0-all")
                .displayName("All Endpoints")
                .pathsToMatch("/api/**")
                .build();
    }
}
