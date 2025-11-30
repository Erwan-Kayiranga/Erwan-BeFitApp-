package com.befit.befit.controller;

import com.befit.befit.model.Exercise;
import com.befit.befit.model.ExerciseType;
import com.befit.befit.model.TrainingSession;
import com.befit.befit.repository.ExerciseRepository;
import com.befit.befit.repository.ExerciseTypeRepository;
import com.befit.befit.repository.TrainingSessionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Controller
public class TrainingSessionController {

    private final TrainingSessionRepository sessionRepo;
    private final ExerciseTypeRepository typeRepo;
    private final ExerciseRepository exerciseRepo;

    public TrainingSessionController(
            TrainingSessionRepository sessionRepo,
            ExerciseTypeRepository typeRepo,
            ExerciseRepository exerciseRepo) {
        this.sessionRepo = sessionRepo;
        this.typeRepo = typeRepo;
        this.exerciseRepo = exerciseRepo;
    }

    // ----------------------- LIST ALL SESSIONS -----------------------
    @GetMapping("/sessions")
    public String listSessions(
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate,
            @RequestParam(name = "sort", required = false, defaultValue = "desc") String sort,
            Model model,
            Authentication auth
    ) {
        String username = auth.getName();
        List<TrainingSession> sessions = sessionRepo.findByUsername(username);

        // ------- FILTER BY DATE RANGE -------
        if (startDate != null) {
            sessions.removeIf(s -> s.getDate().isBefore(startDate));
        }
        if (endDate != null) {
            sessions.removeIf(s -> s.getDate().isAfter(endDate));
        }

        // ------- SORTING -------
        if (sort.equals("asc")) {
            sessions.sort(Comparator.comparing(TrainingSession::getDate));
        } else {
            sessions.sort(Comparator.comparing(TrainingSession::getDate).reversed());
        }

        model.addAttribute("sessions", sessions);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("sort", sort);

        return "session/list";
    }

    // ----------------------- CREATE NEW SESSION -----------------------
    @GetMapping("/sessions/new")
    public String newSession(Model model) {
        model.addAttribute("trainingSession", new TrainingSession());
        return "session/new";
    }

    @PostMapping("/sessions/new")
    public String createSession(
            @ModelAttribute TrainingSession trainingSession,
            Authentication auth) {

        trainingSession.setUsername(auth.getName());
        sessionRepo.save(trainingSession);

        return "redirect:/sessions";
    }

    // ----------------------- ADD EXERCISE TO SESSION -----------------------
    @GetMapping("/sessions/{id}/exercises/add")
    public String addExerciseForm(@PathVariable Long id, Model model) {
        model.addAttribute("exercise", new Exercise());
        model.addAttribute("types", typeRepo.findAll());
        model.addAttribute("sessionId", id);
        return "exercise/add";
    }

    @PostMapping("/sessions/{id}/exercises/add")
    public String saveExercise(
            @PathVariable Long id,
            @ModelAttribute Exercise exercise) {

        TrainingSession session = sessionRepo.findById(id).orElseThrow();
        exercise.setTrainingSession(session);

        exerciseRepo.save(exercise);

        return "redirect:/sessions";
    }
}
