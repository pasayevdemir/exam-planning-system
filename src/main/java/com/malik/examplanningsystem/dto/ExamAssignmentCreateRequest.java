package com.malik.examplanningsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for manually assigning a student to an exam")
public class ExamAssignmentCreateRequest {

    @NotNull(message = "Exam ID is required")
    @Schema(description = "ID of the exam", example = "1")
    private Long examId;

    @NotNull(message = "Student ID is required")
    @Schema(description = "ID of the student being assigned", example = "10")
    private Long studentId;

    @NotNull(message = "Classroom ID is required")
    @Schema(description = "ID of the classroom the student is assigned to", example = "2")
    private Long classroomId;

    @Min(value = 1, message = "Seat number must be at least 1")
    @Schema(description = "Seat number within the classroom", example = "15")
    private Integer seatNumber;
}
