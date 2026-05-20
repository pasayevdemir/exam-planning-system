package com.malik.examplanningsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvigilatorAssignmentResponse {

    private Long invigilationId;
    private Long examId;
    private String examName;
    private LocalDate examDate;
    private LocalTime examTime;
    private Long instructorId;
    private String instructorStaffNo;
    private String instructorName;
    private Long classroomId;
    private String classroomName;
    private LocalDateTime createdAt;
}
