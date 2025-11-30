package com.befit.befit.controller;

import com.befit.befit.model.TrainingSession;
import com.befit.befit.repository.TrainingSessionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/sessions")
public class TrainingSessionController {

    private final TrainingSessionRepository trainingSessionRepository;

    public TrainingSessionController(TrainingSessionRepository repo) {
        this.trainingSessionRepository = repo;
    }

    @GetMapping
    public String list(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "desc") String sort,
            Authentication auth,
            Model model) {

        String username = auth.getName();

        List<TrainingSession> sessions =
                sort.equals("asc") ?
                        trainingSessionRepository.findByUsernameOrderByDateAsc(username) :
                        trainingSessionRepository.findByUsernameOrderByDateDesc(username);

        if (startDate != null)
            sessions.removeIf(s -> s.getDate().isBefore(startDate));

        if (endDate != null)
            sessions.removeIf(s -> s.getDate().isAfter(endDate));

        model.addAttribute("sessions", sessions);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("sort", sort);

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
        trainingSessionRepository.save(session);
        return "redirect:/sessions";
    }
}
