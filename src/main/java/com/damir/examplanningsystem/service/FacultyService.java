package com.malik.examplanningsystem.service;

import com.malik.examplanningsystem.dto.FacultyCreateRequest;
import com.malik.examplanningsystem.dto.FacultyResponse;
import com.malik.examplanningsystem.entity.Faculty;
import com.malik.examplanningsystem.exception.DuplicateResourceException;
import com.malik.examplanningsystem.exception.ResourceNotFoundException;
import com.malik.examplanningsystem.repository.FacultyRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Transactional
    public FacultyResponse createFaculty(FacultyCreateRequest request){
        if(facultyRepository.existsByFacultyName(request.getFacultyName())){
            throw new DuplicateResourceException(
                    "Faculty with name '" + request.getFacultyName() + "' already exists");
        }

        Faculty faculty = new Faculty();
        faculty.setFacultyName(request.getFacultyName());
        return convertToResponse(facultyRepository.save(faculty));
    }

    public List<FacultyResponse> getAllFaculties(){
        return facultyRepository.findAll().stream()
                .map(this::convertToResponse)
                .toList();
    }

    public FacultyResponse getFacultyById(Long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Faculty not found with id: " + id
                ));

        return convertToResponse(faculty);
    }


    @Transactional
    public FacultyResponse updateFaculty(Long id, FacultyCreateRequest request){
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Faculty not found with id: " + id
                ));

        if(!faculty.getFacultyName().equals(request.getFacultyName()) &&
        facultyRepository.existsByFacultyName(request.getFacultyName())){
            throw new DuplicateResourceException(
                    "Faculty with name '" + request.getFacultyName() + "' already exists"
            );
        }

        faculty.setFacultyName(request.getFacultyName());

        Faculty updatedFaculty = facultyRepository.save(faculty);
        return convertToResponse(updatedFaculty);
    }

    @Transactional
    public void deleteFaculty(Long id){
        if(!facultyRepository.existsById(id)){
            throw new ResourceNotFoundException(
                    "Faculty not found with id: " + id
            );
        }
        facultyRepository.deleteById(id);
    }

    private FacultyResponse convertToResponse(Faculty faculty){
        return new FacultyResponse(
                faculty.getFacultyId(),
                faculty.getFacultyName(),
                faculty.getCreatedAt()
        );
    }

    public Faculty getFacultyEntityById(Long id){
        return facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Faculty not found with id: " + id
                ));
    }
}
