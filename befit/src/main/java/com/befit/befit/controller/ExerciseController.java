package com.befit.befit.controller;

import com.befit.befit.model.Exercise;
import com.befit.befit.model.TrainingSession;
import com.befit.befit.repository.ExerciseRepository;
import com.befit.befit.repository.TrainingSessionRepository;
import com.befit.befit.repository.ExerciseTypeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/exercises")
public class ExerciseController {

    private final ExerciseRepository exerciseRepository;
    private final TrainingSessionRepository trainingSessionRepository;
    private final ExerciseTypeRepository exerciseTypeRepository;

    public ExerciseController(
            ExerciseRepository exerciseRepository,
            TrainingSessionRepository trainingSessionRepository,
            ExerciseTypeRepository exerciseTypeRepository
    ) {
        this.exerciseRepository = exerciseRepository;
        this.trainingSessionRepository = trainingSessionRepository;
        this.exerciseTypeRepository = exerciseTypeRepository;
    }

    @GetMapping("/add/{sessionId}")
    public String addForm(@PathVariable Long sessionId, Model model) {
        Exercise e = new Exercise();
        model.addAttribute("exercise", e);
        model.addAttribute("types", exerciseTypeRepository.findAll());
        model.addAttribute("sessionId", sessionId);
        return "exercise/form";
    }

    @PostMapping("/add/{sessionId}")
    public String add(@PathVariable Long sessionId, @ModelAttribute Exercise exercise) {
        TrainingSession session =
                trainingSessionRepository.findById(sessionId).orElseThrow();
        session.addExercise(exercise);

        exerciseRepository.save(exercise);
        TrainingSessionRepository.save(session)

        return "redirect:/sessions/" + sessionId;
    }
}
