package com.befit.befit.controller;

import com.befit.befit.model.Exercise;
import com.befit.befit.model.TrainingSession;
import com.befit.befit.repository.TrainingSessionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class GraphsController {

    private final TrainingSessionRepository sessionRepo;

    public GraphsController(TrainingSessionRepository sessionRepo) {
        this.sessionRepo = sessionRepo;
    }

    @GetMapping("/progress")
    public String showGraphs(Model model, Authentication auth) {

        String username = auth.getName();
        List<TrainingSession> sessions = sessionRepo.findByUsername(username);

        // Sort by date
        sessions.sort(Comparator.comparing(TrainingSession::getDate));

        // Lists for Chart.js
        List<String> dates = new ArrayList<>();
        List<Integer> repsData = new ArrayList<>();
        List<Double> weightData = new ArrayList<>();

        for (TrainingSession s : sessions) {
            dates.add(s.getDate().toString());

            int totalReps = s.getExercises().stream()
                    .mapToInt(Exercise::getReps)
                    .sum();

            double totalWeight = s.getExercises().stream()
                    .mapToDouble(e -> e.getWeight() == null ? 0.0 : e.getWeight())
                    .sum();

            repsData.add(totalReps);
            weightData.add(totalWeight);
        }

        model.addAttribute("dates", dates);
        model.addAttribute("repsData", repsData);
        model.addAttribute("weightData", weightData);

        return "progress/index";
    }
}
