package com.befit.befit.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TrainingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    private String notes;

    @Column(nullable = false)
    private String username;

    @OneToMany(mappedBy = "trainingSession", cascade = CascadeType.ALL)
    private List<Exercise> exercises = new ArrayList<>();

    // helper
    public void addExercise(Exercise e) {
        exercises.add(e);
        e.setTrainingSession(this);
    }

    // getters
    public Long getId() { return id; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public String getNotes() { return notes; }
    public String getUsername() { return username; }
    public List<Exercise> getExercises() { return exercises; }

    // setters
    public void setId(Long id) { this.id = id; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setUsername(String username) { this.username = username; }
    public void setExercises(List<Exercise> exercises) { this.exercises = exercises; }
}
