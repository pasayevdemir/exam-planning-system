package com.malik.examplanningsystem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamCreateRequest {

    @NotBlank(message = "Exam name is required")
    @Size(max = 150, message = "Exam name must not exceed 150 characters")
    private String examName;

    @Size(max = 50, message = "Exam type must not exceed 50 characters")
    private String examType;

    @NotNull(message = "Exam date is required")
    private LocalDate examDate;

    @NotNull(message = "Exam time is required")
    private LocalTime examTime;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer duration;

    @NotNull(message = "Course ID is required")
    private Long courseId;

    @NotNull(message = "Classroom ID is required")
    private Long classroomId;

    private Boolean isCommonExam = false;
}
