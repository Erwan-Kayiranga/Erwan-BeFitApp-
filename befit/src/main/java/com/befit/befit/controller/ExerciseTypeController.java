package com.befit.befit.controller;

import com.befit.befit.model.ExerciseType;
import com.befit.befit.repository.ExerciseTypeRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/types")
public class ExerciseTypeController {

    private final ExerciseTypeRepository repo;

    public ExerciseTypeController(ExerciseTypeRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("types", repo.findAll());
        return "types/list";
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createForm(Model model) {
        model.addAttribute("type", new ExerciseType());
        return "types/create";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String save(@ModelAttribute ExerciseType type) {
        repo.save(type);
        return "redirect:/types";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model model) {
        ExerciseType type = repo.findById(id).orElseThrow();
        model.addAttribute("type", type);
        return "types/edit";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(@PathVariable Long id, @ModelAttribute ExerciseType type) {
        type.setId(id);
        repo.save(type);
        return "redirect:/types";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/types";
    }
}
