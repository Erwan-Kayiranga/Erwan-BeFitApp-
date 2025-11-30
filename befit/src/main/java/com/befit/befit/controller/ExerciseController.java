package com.befit.befit.controller;

import com.befit.befit.model.Exercise;
import com.befit.befit.model.TrainingSession;
import com.befit.befit.repository.ExerciseRepository;
import com.befit.befit.repository.ExerciseTypeRepository;
import com.befit.befit.repository.TrainingSessionRepository;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/exercises")
public class ExerciseController {

    private final ExerciseRepository exerciseRepo;
    private final ExerciseTypeRepository typeRepo;
    private final TrainingSessionRepository sessionRepo;

    public ExerciseController(
            ExerciseRepository exerciseRepo,
            ExerciseTypeRepository typeRepo,
            TrainingSessionRepository sessionRepo) {
        this.exerciseRepo = exerciseRepo;
        this.typeRepo = typeRepo;
        this.sessionRepo = sessionRepo;
    }

    @GetMapping("/create/{sessionId}")
    public String showCreateForm(@PathVariable Long sessionId,
                                 Authentication auth,
                                 Model model) {

        TrainingSession session = sessionRepo.findById(sessionId).orElse(null);
        if (session == null || !session.getUsername().equals(auth.getName()))
            return "redirect:/sessions";

        Exercise e = new Exercise();
        e.setTrainingSession(session);

        model.addAttribute("exercise", e);
        model.addAttribute("types", typeRepo.findAll());
        return "exercises/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("exercise") Exercise exercise,
                         BindingResult result,
                         Authentication auth,
                         Model model) {

        TrainingSession session = sessionRepo.findById(exercise.getTrainingSession().getId())
                .orElse(null);

        if (session == null || !session.getUsername().equals(auth.getName()))
            return "redirect:/sessions";

        if (result.hasErrors()) {
            model.addAttribute("types", typeRepo.findAll());
            return "exercises/create";
        }

        exerciseRepo.save(exercise);
        return "redirect:/sessions";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Authentication auth) {
        Exercise ex = exerciseRepo.findById(id).orElse(null);

        if (ex != null && ex.getTrainingSession().getUsername().equals(auth.getName()))
            exerciseRepo.delete(ex);

        return "redirect:/sessions";
    }
}
