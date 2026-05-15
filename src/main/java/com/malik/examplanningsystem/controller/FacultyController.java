package com.malik.examplanningsystem.controller;

import com.malik.examplanningsystem.dto.FacultyCreateRequest;
import com.malik.examplanningsystem.dto.FacultyResponse;
import com.malik.examplanningsystem.service.FacultyService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/faculties")
public class FacultyController {

    private final FacultyService facultyService;

    @PostMapping
    public ResponseEntity<FacultyResponse> createFaculty(
            @Valid @RequestBody FacultyCreateRequest facultyCreateRequest) {
        FacultyResponse facultyResponse = facultyService.createFaculty(facultyCreateRequest);
        return new  ResponseEntity<>(facultyResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FacultyResponse>> getAllFaculties() {
        List<FacultyResponse> facultyResponses = facultyService.getAllFaculties();
        return ResponseEntity.ok(facultyResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacultyResponse> getFacultyById(@PathVariable Long id) {
        FacultyResponse facultyResponse = facultyService.getFacultyById(id);
        return ResponseEntity.ok(facultyResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacultyResponse> updateFaculty(
            @PathVariable Long id,
            @Valid @RequestBody FacultyCreateRequest facultyCreateRequest
    ){
        FacultyResponse updated =  facultyService.updateFaculty(id, facultyCreateRequest);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id){
        facultyService.deleteFaculty(id);
        return ResponseEntity.noContent().build();
    }
}
