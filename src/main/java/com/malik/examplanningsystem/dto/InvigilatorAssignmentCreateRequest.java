package com.malik.examplanningsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for manually assigning an instructor as invigilator")
public class InvigilatorAssignmentCreateRequest {

    @NotNull(message = "Exam ID is required")
    @Schema(description = "ID of the exam to invigilate", example = "1")
    private Long examId;

    @NotNull(message = "Instructor ID is required")
    @Schema(description = "ID of the instructor acting as invigilator", example = "4")
    private Long instructorId;

    @NotNull(message = "Classroom ID is required")
    @Schema(description = "ID of the classroom the invigilator is responsible for", example = "2")
    private Long classroomId;
}
