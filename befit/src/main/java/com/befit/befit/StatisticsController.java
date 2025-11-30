package com.befit.befit;

import com.befit.befit.model.TrainingSession;
import com.befit.befit.repository.TrainingSessionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class StatisticsController {

    private final TrainingSessionRepository trainingSessionRepository;

    public StatisticsController(TrainingSessionRepository trainingSessionRepository) {
        this.trainingSessionRepository = trainingSessionRepository;
    }

    @GetMapping("/stats")
    public String stats(Model model, Authentication auth) {

        String username = auth.getName();
        List<TrainingSession> sessions = trainingSessionRepository.findByUsername(username);

        model.addAttribute("sessions", sessions);

        return "stats/charts";
    }
}
