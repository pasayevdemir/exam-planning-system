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
public class ExamAssignmentResponse {

    private Long assignmentId;
    private Long examId;
    private String examName;
    private LocalDate examDate;
    private LocalTime examTime;
    private Long studentId;
    private String studentNo;
    private String studentName;
    private Long classroomId;
    private String classroomName;
    private Integer seatNumber;
    private LocalDateTime createdAt;
}
