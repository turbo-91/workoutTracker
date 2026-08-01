package com.tjost.workouttracker.controller;

import com.tjost.workouttracker.dto.WorkoutPlanRequest;
import com.tjost.workouttracker.model.Exercise;
import com.tjost.workouttracker.model.WorkoutPlan;
import com.tjost.workouttracker.model.enums.Day;
import com.tjost.workouttracker.repository.ExerciseRepository;
import com.tjost.workouttracker.repository.WorkoutPlanRepository;
import com.tjost.workouttracker.service.ExerciseService;
import com.tjost.workouttracker.service.WorkoutPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workout-plans")
@RequiredArgsConstructor
public class WorkoutPlanController {

    private final WorkoutPlanRepository planRepo;
    private final WorkoutPlanService planService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkoutPlan createWorkoutPlan(@Valid @RequestBody WorkoutPlanRequest request) {
        return planService.createWorkoutPlan(
                request.day(),
                request.name(),
                request.comment()
        );
    }

    @GetMapping
    public List<WorkoutPlan> getAllWorkoutPlans() {
        return planService.getAllWorkoutPlans();
    }

    @GetMapping("/{id}")
    public WorkoutPlan getWorkoutPlanById(@PathVariable Long id) {
        return planService.getWorkoutPlanById(id);
    }

    @PutMapping("/{id}")
    public WorkoutPlan updateWorkoutPlan(
            @PathVariable Long id,
            @RequestBody WorkoutPlanRequest request
    ) {
        return planService.updateWorkoutPlan(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorkoutPlan(@PathVariable Long id) {
        planService.deleteWorkoutPlan(id);
    }
}