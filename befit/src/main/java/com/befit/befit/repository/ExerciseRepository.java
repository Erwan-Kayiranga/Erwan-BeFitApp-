package com.befit.befit.repository;

import com.befit.befit.model.Exercise;
import com.befit.befit.model.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    List<Exercise> findByTrainingSession(TrainingSession trainingSession);
}
