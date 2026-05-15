package com.malik.examplanningsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FacultyResponse {
    
    private Long facultyId;
    private String facultyName;
    private LocalDateTime createdAt;
}
