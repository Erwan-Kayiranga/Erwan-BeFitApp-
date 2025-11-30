package com.befit.befit.controller;

import com.befit.befit.model.Exercise;
import com.befit.befit.repository.TrainingSessionRepository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class StatisticsController {

    private final TrainingSessionRepository repo;

    public StatisticsController(TrainingSessionRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/stats")
    public String stats(Authentication auth, Model model) {

        LocalDateTime since = LocalDateTime.now().minusWeeks(4);

        List<TrainingSession> sessions =
                repo.findByUsernameAndStartTimeAfter(auth.getName(), since);

        Map<String, Long> countByType =
                sessions.stream()
                        .flatMap(s -> s.getExercises().stream())
                        .collect(Collectors.groupingBy(
                                e -> e.getExerciseType().getName(),
                                Collectors.counting()
                        ));

        Map<String, Integer> totalRepsByType =
                sessions.stream()
                        .flatMap(s -> s.getExercises().stream())
                        .collect(Collectors.groupingBy(
                                e -> e.getExerciseType().getName(),
                                Collectors.summingInt(Exercise::getTotalReps)
                        ));

        Map<String, Double> maxWeightByType =
                sessions.stream()
                        .flatMap(s -> s.getExercises().stream())
                        .collect(Collectors.groupingBy(
                                e -> e.getExerciseType().getName(),
                                Collectors.collectingAndThen(
                                        Collectors.mapping(
                                                Exercise::getWeight,
                                                Collectors.maxBy(Double::compare)
                                        ),
                                        opt -> opt.orElse(0.0)
                                )
                        ));

        Map<String, Double> avgWeightByType =
                sessions.stream()
                        .flatMap(s -> s.getExercises().stream())
                        .collect(Collectors.groupingBy(
                                e -> e.getExerciseType().getName(),
                                Collectors.averagingDouble(Exercise::getWeight)
                        ));

        model.addAttribute("countByType", countByType);
        model.addAttribute("totalRepsByType", totalRepsByType);
        model.addAttribute("avgWeightByType", avgWeightByType);
        model.addAttribute("maxWeightByType", maxWeightByType);

        return "stats";
    }
}
