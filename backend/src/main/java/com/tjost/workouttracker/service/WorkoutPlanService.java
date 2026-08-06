package com.tjost.workouttracker.service;

import com.tjost.workouttracker.dto.WorkoutPlanDetailsDTO;
import com.tjost.workouttracker.dto.WorkoutPlanItemDetailsDTO;
import com.tjost.workouttracker.dto.WorkoutPlanRequest;
import com.tjost.workouttracker.exception.PlanAlreadyExistsException;
import com.tjost.workouttracker.exception.PlanNotFoundException;
import com.tjost.workouttracker.model.WorkoutPlan;
import com.tjost.workouttracker.model.WorkoutPlanItem;
import com.tjost.workouttracker.model.enums.Day;
import com.tjost.workouttracker.repository.WorkoutPlanItemRepository;
import com.tjost.workouttracker.repository.WorkoutPlanRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutPlanService {

    private final WorkoutPlanRepository planRepo;
    private final WorkoutPlanItemRepository itemRepo;

    public WorkoutPlanDetailsDTO getWorkoutPlanDetailsById(Long planId) {
        List<WorkoutPlanItem> items = itemRepo.findAllByWorkoutPlan_PlanIdOrderByExercisePositionAsc(planId);
        List<WorkoutPlanItemDetailsDTO> itemDTOs = createItemDTOs(items);
        WorkoutPlan workoutPlan = getWorkoutPlanById(planId);
        return new WorkoutPlanDetailsDTO(
                workoutPlan.getDay(),
                workoutPlan.getName(),
                itemDTOs,
                workoutPlan.getComment()); // what if no comment?
    }

    public static List<WorkoutPlanItemDetailsDTO> createItemDTOs(List<WorkoutPlanItem> items) {
        List<WorkoutPlanItemDetailsDTO> itemDTOs = new ArrayList<>();
        for (WorkoutPlanItem item : items) {
            WorkoutPlanItemDetailsDTO itemDTO = new WorkoutPlanItemDetailsDTO(
                    item.getExercisePosition(),
                    item.getExercise().getName(),
                    item.getTargetWeightKg(),
                    item.getTargetRepMin(),
                    item.getTargetRepMax(),
                    item.getComment() // what if no comment?
            );
            itemDTOs.add(itemDTO);
        }
        return itemDTOs;
    }

    public List<WorkoutPlan> getAllWorkoutPlans() {
        return planRepo.findAll();
    }

    public WorkoutPlan getWorkoutPlanById(Long planId) {
        return planRepo.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(planId));
    }

    public WorkoutPlan createWorkoutPlan(Day day, String name, String comment) {
        if (planRepo.existsByNameIgnoreCase(name)) {
            throw new PlanAlreadyExistsException(name);
        }
        WorkoutPlan plan = WorkoutPlan.builder()
                .day(day)
                .name(name)
                .comment(comment)
                .build();

        return planRepo.save(plan);
    };

    @Transactional
    public WorkoutPlan updateWorkoutPlan(
            Long planId,
            WorkoutPlanRequest updatedPlan
    ) {
        WorkoutPlan existingPlan = planRepo.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(planId));

        existingPlan.setDay(updatedPlan.day());
        existingPlan.setName(updatedPlan.name());
        existingPlan.setComment(updatedPlan.comment());

        return existingPlan;
    }

    @Transactional
    public void deleteWorkoutPlan(Long planId) {
      WorkoutPlan plan = planRepo.findById(planId)
              .orElseThrow(() -> new PlanNotFoundException(planId));
    planRepo.delete(plan);
    };


}
