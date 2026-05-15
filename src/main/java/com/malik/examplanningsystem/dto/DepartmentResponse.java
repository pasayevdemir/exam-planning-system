package com.malik.examplanningsystem.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponse {
    private Long departmentId;
    private String departmentName;
    private Long facultyId;
    private String facultyName;
}
