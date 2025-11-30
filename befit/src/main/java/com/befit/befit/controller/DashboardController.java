package com.befit.befit.controller;

import com.befit.befit.repository.TrainingSessionRepository;
import com.befit.befit.model.TrainingSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final TrainingSessionRepository sessionRepo;

    public DashboardController(TrainingSessionRepository sessionRepo) {
        this.sessionRepo = sessionRepo;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {

        String username = auth.getName();
        List<TrainingSession> sessions = sessionRepo.findByUsername(username);

        int totalExercises = sessions.stream()
                .mapToInt(s -> s.getExercises().size())
                .sum();

        int totalReps = sessions.stream()
                .flatMap(s -> s.getExercises().stream())
                .mapToInt(e -> e.getReps())
                .sum();

        double totalWeight = sessions.stream()
                .flatMap(s -> s.getExercises().stream())
                .mapToDouble(e -> e.getWeight() == null ? 0.0 : e.getWeight())
                .sum();

        model.addAttribute("sessions", sessions);
        model.addAttribute("totalExercises", totalExercises);
        model.addAttribute("totalReps", totalReps);
        model.addAttribute("totalWeight", totalWeight);
        model.addAttribute("totalSessions", sessions.size());

        return "dashboard/index";
    }
}
