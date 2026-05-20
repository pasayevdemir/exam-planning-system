package com.malik.examplanningsystem.service;

import com.malik.examplanningsystem.dto.ClassroomCreateRequest;
import com.malik.examplanningsystem.dto.ClassroomResponse;
import com.malik.examplanningsystem.entity.Classroom;
import com.malik.examplanningsystem.exception.ResourceNotFoundException;
import com.malik.examplanningsystem.repository.ClassroomRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClassroomService {
    private final ClassroomRepository classroomRepository;

    @Transactional
    public ClassroomResponse createClassroom(ClassroomCreateRequest request){
        Classroom classroom = new Classroom();

        classroom.setCampus(request.getCampus());
        classroom.setBuilding(request.getBuilding());
        classroom.setRoomName(request.getRoomName());
        classroom.setCapacity(request.getCapacity());
        classroom.setIsAvailable(request.getIsAvailable());
        classroom.setTechnicalFeatures(request.getTechnicalFeatures());

        Classroom savedClassroom = classroomRepository.save(classroom);
        return convertToResponse(savedClassroom);
    }

    public List<ClassroomResponse> getAllClassrooms(){
        return classroomRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    public ClassroomResponse getClassroomById(Long id) {
        Classroom classroom = getClassroomEntityById(id);
        return convertToResponse(classroom);
    }

    public List<ClassroomResponse> getAvailableClassrooms(Integer minCapacity){
        return classroomRepository.findByCapacityGreaterThanEqual(minCapacity)
                .stream()
                .filter(Classroom::getIsAvailable)
                .map(this::convertToResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    @Transactional
    public ClassroomResponse updateClassroom(Long id, ClassroomCreateRequest request){
        Classroom classroom = getClassroomEntityById(id);

        classroom.setCampus(request.getCampus());
        classroom.setBuilding(request.getBuilding());
        classroom.setRoomName(request.getRoomName());
        classroom.setCapacity(request.getCapacity());
        classroom.setIsAvailable(request.getIsAvailable());
        classroom.setTechnicalFeatures(request.getTechnicalFeatures());

        Classroom updatedClassroom = classroomRepository.save(classroom);
        return convertToResponse(updatedClassroom);
    }

    @Transactional
    public void deleteClassroom(Long id){
        if(!classroomRepository.existsById(id)){
            throw new ResourceNotFoundException("Classroom not found with id: " + id);
        }
        classroomRepository.deleteById(id);
    }

    public Classroom getClassroomEntityById(Long id){
        return classroomRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Classroom not found with id: " + id));
    }

    private ClassroomResponse convertToResponse(Classroom classroom){
        return new ClassroomResponse(
                classroom.getClassroomId(),
                classroom.getCampus(),
                classroom.getBuilding(),
                classroom.getRoomName(),
                classroom.getCapacity(),
                classroom.getIsAvailable(),
                classroom.getTechnicalFeatures(),
                classroom.getCreatedAt()
        );
    }
}
