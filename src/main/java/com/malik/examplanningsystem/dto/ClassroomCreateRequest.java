package com.malik.examplanningsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for creating or updating a classroom")
public class ClassroomCreateRequest {

    @NotBlank(message = "Campus name is required")
    @Size(max = 100)
    @Schema(description = "Campus where the classroom is located", example = "Main Campus")
    private String campus;

    @NotBlank(message = "Building name is required")
    @Size(max = 100)
    @Schema(description = "Building name", example = "Engineering Block A")
    private String building;

    @NotBlank(message = "Room name is required")
    @Size(max = 50)
    @Schema(description = "Room identifier", example = "A-101")
    private String roomName;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    @Schema(description = "Maximum student capacity of the room", example = "60")
    private Integer capacity;

    @Schema(description = "Whether this room is available for exam scheduling", example = "true")
    private Boolean isAvailable;

    @Size(max = 255)
    @Schema(description = "Optional notes about technical features (projector, AC, etc.)", example = "Air-conditioned, projector available")
    private String technicalFeatures;
}
