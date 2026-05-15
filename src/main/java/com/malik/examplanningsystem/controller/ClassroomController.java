package com.malik.examplanningsystem.controller;

import com.malik.examplanningsystem.dto.ClassroomCreateRequest;
import com.malik.examplanningsystem.dto.ClassroomResponse;
import com.malik.examplanningsystem.service.ClassroomService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/classrooms")
public class ClassroomController {

    private final ClassroomService classroomService;

    @PostMapping
    public ResponseEntity<ClassroomResponse> createClassroom(
            @Valid @RequestBody ClassroomCreateRequest request){
        ClassroomResponse response = classroomService.createClassroom(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ClassroomResponse>> getAllClassrooms(){
        return ResponseEntity.ok(classroomService.getAllClassrooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassroomResponse> getClassroomById(@PathVariable Long id){
        return ResponseEntity.ok(classroomService.getClassroomById(id));
    }

    @GetMapping("/available")
    public ResponseEntity<List<ClassroomResponse>> getAvailableClassrooms(
            @RequestParam(required = false, defaultValue = "1") Integer minCapacity){
        return ResponseEntity.ok(classroomService.getAvailableClassrooms(minCapacity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassroomResponse> updateClassroom(
            @PathVariable Long id,
            @Valid @RequestBody ClassroomCreateRequest request){
        return ResponseEntity.ok(classroomService.updateClassroom(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassroom(@PathVariable Long id){
        classroomService.deleteClassroom(id);
        return ResponseEntity.noContent().build();
    }


}
