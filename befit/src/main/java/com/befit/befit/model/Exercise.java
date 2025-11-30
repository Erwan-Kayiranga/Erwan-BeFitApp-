package com.befit.befit.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exercise_type_id", nullable = false)
    private ExerciseType exerciseType;

    @ManyToOne
    @JoinColumn(name = "training_session_id", nullable = false)
    private TrainingSession trainingSession;

    // 👉 number of sets
    @NotNull
    @Min(1)
    private Integer sets;

    // 👉 repetitions per set
    @NotNull
    @Min(1)
    private Integer repsPerSet;

    // 👉 used weight (can be 0 for bodyweight)
    @NotNull
    @Min(0)
    private Double weight;

    // ---------- GETTERS & SETTERS ----------

    public Long getId() { return id; }

    public ExerciseType getExerciseType() {
        return exerciseType;
    }
    public void setExerciseType(ExerciseType exerciseType) {
        this.exerciseType = exerciseType;
    }

    public TrainingSession getTrainingSession() {
        return trainingSession;
    }
    public void setTrainingSession(TrainingSession trainingSession) {
        this.trainingSession = trainingSession;
    }

    public Integer getSets() {
        return sets;
    }
    public void setSets(Integer sets) {
        this.sets = sets;
    }

    public Integer getRepsPerSet() {
        return repsPerSet;
    }
    public void setRepsPerSet(Integer repsPerSet) {
        this.repsPerSet = repsPerSet;
    }

    public Double getWeight() {
        return weight;
    }
    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
