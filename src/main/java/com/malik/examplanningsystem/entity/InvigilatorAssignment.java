package com.malik.examplanningsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "invigilator_assignments", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"exam_id", "instructor_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvigilatorAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invigilationId;

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private Instructor instructor;

    @ManyToOne
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom classroom;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
