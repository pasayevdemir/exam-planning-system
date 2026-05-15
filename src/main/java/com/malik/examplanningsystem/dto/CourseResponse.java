package com.malik.examplanningsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {

    private Long courseId;
    private String courseCode;
    private String courseName;
    private Long instructorId;
    private String instructorName;
    private Long departmentId;
    private String departmentName;
    private Integer creditHours;
    private String semester;
    private LocalDateTime createdAt;
}
