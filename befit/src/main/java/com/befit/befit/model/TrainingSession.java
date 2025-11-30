package com.befit.befit.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TrainingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate date;

    private String notes;

    @Column(nullable = false)
    private String username;

    @OneToMany(mappedBy = "trainingSession", cascade = CascadeType.ALL)
    private List<Exercise> exercises = new ArrayList<>();

    public void addExercise(Exercise e) {
        exercises.add(e);
        e.setTrainingSession(this);
    }

    public double getTotalWeight() {
        return exercises.stream()
                .mapToDouble(e -> e.getWeight() != null ? e.getWeight() : 0)
                .sum();
    }

    public int getTotalReps() {
        return exercises.stream()
                .mapToInt(Exercise::getReps)
                .sum();
    }

    public int getExerciseCount() {
        return exercises.size();
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public List<Exercise> getExercises() { return exercises; }
    public void setExercises(List<Exercise> exercises) { this.exercises = exercises; }
}
