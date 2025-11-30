package com.befit.befit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {

    List<TrainingSession> findByUsernameOrderByStartTimeDesc(String username);
    List<TrainingSession> findByUsernameOrderByStartTimeAsc(String username);
}
