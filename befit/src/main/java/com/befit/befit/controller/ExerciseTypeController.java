package com.befit.befit.controller;

import com.befit.befit.model.ExerciseType;
import com.befit.befit.repository.ExerciseTypeRepository;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/types")
@PreAuthorize("hasRole('ADMIN')")
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

    @GetMapping("/new")
    public String newType(Model model) {
        model.addAttribute("type", new ExerciseType());
        return "types/form";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("type") ExerciseType type,
                         BindingResult result) {
        if (result.hasErrors()) return "types/form";
        repo.save(type);
        return "redirect:/types";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("type", repo.findById(id).orElseThrow());
        return "types/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("type") ExerciseType type,
                         BindingResult result) {
        if (result.hasErrors()) return "types/form";
        type.setId(id);
        repo.save(type);
        return "redirect:/types";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/types";
    }
}
