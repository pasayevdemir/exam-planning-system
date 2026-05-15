package com.malik.examplanningsystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvigilatorAssignmentCreateRequest {

    @NotNull(message = "Exam ID is required")
    private Long examId;

    @NotNull(message = "Instructor ID is required")
    private Long instructorId;

    @NotNull(message = "Classroom ID is required")
    private Long classroomId;
}
