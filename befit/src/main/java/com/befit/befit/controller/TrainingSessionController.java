package com.befit.befit.controller;

import com.befit.befit.model.TrainingSession;
import com.befit.befit.repository.TrainingSessionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sessions")
public class TrainingSessionController {

    private final TrainingSessionRepository trainingSessionRepository;

    public TrainingSessionController(TrainingSessionRepository trainingSessionRepository) {
        this.trainingSessionRepository = trainingSessionRepository;
    }

    @GetMapping
    public String list(Model model, Authentication auth) {
        String username = auth.getName();
        List<TrainingSession> sessions =
                trainingSessionRepository.findByUsernameOrderByStartTimeDesc(username);

        model.addAttribute("sessions", sessions);
        return "session/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("session", new TrainingSession());
        return "session/form";
    }

    @PostMapping
    public String create(@ModelAttribute TrainingSession session, Authentication auth) {
        session.setUsername(auth.getName());
        S save = TrainingSessionRepository.save(session);
        return "redirect:/sessions";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model, Authentication auth) {
        TrainingSession ts = trainingSessionRepository.findById(id).orElseThrow();
        if (!ts.getUsername().equals(auth.getName()))
            return "redirect:/sessions";
        model.addAttribute("session", ts);
        return "session/details";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Authentication auth) {
        TrainingSession ts = trainingSessionRepository.findById(id).orElseThrow();
        if (!ts.getUsername().equals(auth.getName()))
            return "redirect:/sessions";

        trainingSessionRepository.delete(ts);
        return "redirect:/sessions";
    }
}
