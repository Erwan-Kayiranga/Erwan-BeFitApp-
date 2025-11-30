package com.befit.befit.repository;

import com.befit.befit.model.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {

    List<TrainingSession> findByUsername(String username);

    List<TrainingSession> findByUsernameAndDateAfter(String username, LocalDate since);
}
