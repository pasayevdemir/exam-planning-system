package com.malik.examplanningsystem.dto;

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
public class ClassroomCreateRequest {

    @NotBlank(message = "Campus name is required")
    @Size(max = 100)
    private String campus;

    @NotBlank(message = "Building name is required")
    @Size(max = 100)
    private String building;

    @NotBlank(message = "Room name is required")
    @Size(max = 50)
    private String roomName;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;

    private Boolean isAvailable;

    @Size(max = 255)
    private String technicalFeatures;
}
