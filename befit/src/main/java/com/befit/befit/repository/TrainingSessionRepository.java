package com.befit.befit.repository;

import com.befit.befit.model.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {

    List<TrainingSession> findByUsernameOrderByDateDesc(String username);

    List<TrainingSession> findByUsernameOrderByDateAsc(String username);
}
