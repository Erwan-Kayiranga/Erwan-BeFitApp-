package com.befit.befit.controller;

import com.befit.befit.model.ExerciseType;
import com.befit.befit.repository.ExerciseRepository;
import com.befit.befit.repository.ExerciseTypeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class StatisticsController {

    @PersistenceContext
    private EntityManager em;

    private final ExerciseTypeRepository typeRepo;

    public StatisticsController(ExerciseTypeRepository typeRepo) {
        this.typeRepo = typeRepo;
    }

    @GetMapping("/stats")
    public String stats(Authentication auth, Model model) {

        LocalDate since = LocalDate.now().minusWeeks(4);

        List<Object[]> results = em.createQuery("""
           select et.name, count(e.id)
           from Exercise e
           join e.exerciseType et
           join e.trainingSession ts
           where ts.username = :username
             and ts.date >= :since
           group by et.name
        """, Object[].class)
                .setParameter("username", auth.getName())
                .setParameter("since", since)
                .getResultList();

        model.addAttribute("stats", results);
        return "stats/index";
    }
}
