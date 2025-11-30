package com.befit.befit.repository;

import com.befit.befit.model.Exercise;
import com.befit.befit.dto.ExerciseStatsRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    @Query("""
        select et.name as exerciseName,
               count(distinct e.trainingSession.id) as timesPerformed,
               sum(e.sets * e.repsPerSet) as totalReps,
               avg(e.weight) as avgWeight,
               max(e.weight) as maxWeight
        from Exercise e
        join e.exerciseType et
        join e.trainingSession ts
        where ts.username = :username
          and ts.startTime >= :from
        group by et.name
        order by et.name
        """)
    List<ExerciseStatsRow> findStatsForUserSince(
            @Param("username") String username,
            @Param("from") LocalDateTime from
    );
}
