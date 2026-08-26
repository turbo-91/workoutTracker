package com.tjost.workouttracker.controller;

import com.tjost.workouttracker.dto.WorkoutLogResponse;
import com.tjost.workouttracker.dto.WorkoutPlanDetailsDTO;
import com.tjost.workouttracker.dto.WorkoutPlanRequest;
import com.tjost.workouttracker.dto.WorkoutPlanResponse;
import com.tjost.workouttracker.model.Exercise;
import com.tjost.workouttracker.model.WorkoutLog;
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
@CrossOrigin(origins = "http://localhost:5173")
public class WorkoutPlanController {

    private final WorkoutPlanRepository planRepo;
    private final WorkoutPlanService planService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkoutPlanResponse createWorkoutPlan(@Valid @RequestBody WorkoutPlanRequest request) {
        WorkoutPlan plan = planService.createWorkoutPlan(
                request.day(),
                request.name(),
                request.comment()
        );
        return toResponse(plan);
    }

    @GetMapping
    public List<WorkoutPlanResponse> getAllWorkoutPlans() {
        return planService.getAllWorkoutPlans()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public WorkoutPlanResponse getWorkoutPlanById(@PathVariable Long id) {
        WorkoutPlan plan = planService.getWorkoutPlanById(id);
        return toResponse(plan);
    }

    @GetMapping("/{id}/details")
    public WorkoutPlanDetailsDTO getWorkoutPlanDetailsById(@PathVariable Long id) { return planService.getWorkoutPlanDetailsById(id); }

    @PutMapping("/{id}")
    public WorkoutPlanResponse updateWorkoutPlan(
            @PathVariable Long id,
            @RequestBody WorkoutPlanRequest request
    ) {
        WorkoutPlan plan = planService.updateWorkoutPlan(id, request);
        return toResponse(plan);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorkoutPlan(@PathVariable Long id) {
        planService.deleteWorkoutPlan(id);
    }

    private WorkoutPlanResponse toResponse(WorkoutPlan plan) {
        return new WorkoutPlanResponse(
                plan.getPlanId(),
                plan.getDay(),
                plan.getName(),
                plan.getComment()
        );
    }
}