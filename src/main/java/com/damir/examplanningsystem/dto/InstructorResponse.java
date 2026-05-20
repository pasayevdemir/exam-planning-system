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
public class InstructorResponse {

    private Long instructorId;
    private String staffNo;
    private String fullName;
    private String email;
    private Long departmentId;
    private String departmentName;
    private Long facultyId;
    private String facultyName;
    private Boolean isAvailableForInvigilation;
    private Integer dutyCount;
    private LocalDateTime createdAt;
}
