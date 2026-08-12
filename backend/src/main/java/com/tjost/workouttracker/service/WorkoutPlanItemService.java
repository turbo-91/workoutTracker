package com.tjost.workouttracker.service;

import com.tjost.workouttracker.dto.WorkoutPlanItemRequest;
import com.tjost.workouttracker.exception.ItemNotFoundExceptionPlanId;
import com.tjost.workouttracker.exception.PlanNotFoundException;
import com.tjost.workouttracker.exception.ExerciseNotFoundException;
import com.tjost.workouttracker.model.Exercise;
import com.tjost.workouttracker.model.WorkoutPlan;
import com.tjost.workouttracker.model.WorkoutPlanItem;
import com.tjost.workouttracker.repository.ExerciseRepository;
import com.tjost.workouttracker.repository.WorkoutPlanItemRepository;
import com.tjost.workouttracker.exception.ItemNotFoundException;
import com.tjost.workouttracker.repository.WorkoutPlanRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
public class WorkoutPlanItemService {

    private final ExerciseRepository exerciseRepo;
    private final WorkoutPlanRepository planRepo;
    private final WorkoutPlanItemRepository itemRepo;

    public WorkoutPlanItem getWorkoutPlanItemById(Long planItemId) {
        return itemRepo.findById(planItemId)
                .orElseThrow(() -> new ItemNotFoundException(planItemId));
    }

    public List<WorkoutPlanItem> getWorkoutPlanItemsByPlanId(Long planId) {
        List<WorkoutPlanItem> items =
                itemRepo.findAllByWorkoutPlan_PlanIdOrderByExercisePositionAsc(planId);

        if (items.isEmpty()) {
            throw new ItemNotFoundExceptionPlanId(planId);
        }

        return items;
    }

    public WorkoutPlanItem createWorkoutPlanItem (
            Long planId,
            Long exerciseId,
            short exercisePosition,
            BigDecimal targetWeightKg,
            short targetRepMin,
            short targetRepMax,
            String comment
    ) {
        WorkoutPlan plan = planRepo.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(planId));

        Exercise exercise = exerciseRepo.findById(exerciseId)
                .orElseThrow(() -> new ExerciseNotFoundException(exerciseId));

        WorkoutPlanItem item = WorkoutPlanItem.builder()
                .workoutPlan(plan)
                .exercise(exercise)
                .exercisePosition(exercisePosition)
                .targetWeightKg(targetWeightKg)
                .targetRepMin(targetRepMin)
                .targetRepMax(targetRepMax)
                .comment(comment)
                .build();

        return itemRepo.save(item);
    }

    @Transactional
    public WorkoutPlanItem updateWorkoutPlanItem(
            Long itemId,
            WorkoutPlanItemRequest request
    ) {
        WorkoutPlanItem existingItem = itemRepo.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId));

        existingItem.setExercisePosition(request.exercisePosition());
        existingItem.setTargetWeightKg(request.targetWeightKg());
        existingItem.setTargetRepMin(request.targetRepMin());
        existingItem.setTargetRepMax(request.targetRepMax());
        existingItem.setComment(request.comment());

        return existingItem;
    }

       @Transactional
    public void deleteWorkoutPlanItem(Long itemId) {
        WorkoutPlanItem item = itemRepo.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId));
        itemRepo.delete(item);
       }
    }
