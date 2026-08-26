package com.tjost.workouttracker.controller;

import com.tjost.workouttracker.dto.WorkoutLogResponse;
import com.tjost.workouttracker.dto.WorkoutPlanItemRequest;
import com.tjost.workouttracker.dto.WorkoutPlanItemResponse;
import com.tjost.workouttracker.model.WorkoutLog;
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

    private final WorkoutPlanItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkoutPlanItemResponse createWorkoutPlanItem(@Valid @RequestBody
                                                 WorkoutPlanItemRequest request) {
        WorkoutPlanItem item = itemService.createWorkoutPlanItem(
                request.planId(),
                request.exerciseId(),
                request.exercisePosition(),
                request.targetWeightKg(),
                request.targetRepMin(),
                request.targetRepMax(),
                request.comment());
        return toResponse(item);
    }

    @GetMapping("/{id}")
    public WorkoutPlanItemResponse getWorkoutPlanItemById(@PathVariable Long id){
        WorkoutPlanItem item = itemService.getWorkoutPlanItemById(id);
        return toResponse(item);
    }

    @PutMapping("/{id}")
    public WorkoutPlanItemResponse updateWorkoutPlanItem(
            @PathVariable Long id, @Valid @RequestBody WorkoutPlanItemRequest item) {
        WorkoutPlanItem updatedItem = itemService.updateWorkoutPlanItem(id, item);
        return toResponse(updatedItem);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorkoutPlanItem(@PathVariable Long id) {
        itemService.deleteWorkoutPlanItem(id);
    }

    private WorkoutPlanItemResponse toResponse(WorkoutPlanItem item) {
        return new WorkoutPlanItemResponse(
                item.getPlanItemId(),
                item.getWorkoutPlan().getPlanId(),
                item.getExercise().getExerciseId(),
                item.getExercisePosition(),
                item.getTargetWeightKg(),
                item.getTargetRepMin(),
                item.getTargetRepMax(),
                item.getComment()

        );
    }


}
