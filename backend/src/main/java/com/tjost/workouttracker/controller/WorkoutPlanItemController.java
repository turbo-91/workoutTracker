package com.tjost.workouttracker.controller;

import com.tjost.workouttracker.dto.WorkoutPlanItemRequest;
import com.tjost.workouttracker.model.WorkoutPlanItem;
import com.tjost.workouttracker.repository.WorkoutPlanItemRepository;
import com.tjost.workouttracker.service.WorkoutPlanItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workout-plan-items")
@RequiredArgsConstructor
public class WorkoutPlanItemController {

    private final WorkoutPlanItemRepository itemRepo;
    private final WorkoutPlanItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkoutPlanItem createWorkoutPlanItem(@Valid @RequestBody
                                                 WorkoutPlanItemRequest request) {
        return itemService.createWorkoutPlanItem(
                request.planId(),
                request.exerciseId(),
                request.exercisePosition(),
                request.targetWeightKg(),
                request.targetRepMin(),
                request.targetRepMax(),
                request.comment());
    }

    @GetMapping("/{id}")
    public WorkoutPlanItem getWorkoutPlanItemById(@PathVariable Long id){
        return itemService.getWorkoutPlanItemById(id);
    }

    @PutMapping("/{id}")
    public WorkoutPlanItem updateWorkoutPlanItem(
            @PathVariable Long id, @Valid @RequestBody WorkoutPlanItemRequest item) {
        return itemService.updateWorkoutPlanItem(id, item);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorkoutPlanItem(@PathVariable Long id) {
        itemService.deleteWorkoutPlanItem(id);
    }


}
