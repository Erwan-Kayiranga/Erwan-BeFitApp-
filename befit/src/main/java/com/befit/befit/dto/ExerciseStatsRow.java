package com.befit.befit.dto;

public interface ExerciseStatsRow {

    String getExerciseName();

    long getTimesPerformed();

    long getTotalReps();

    Double getAvgWeight();

    Double getMaxWeight();
}
