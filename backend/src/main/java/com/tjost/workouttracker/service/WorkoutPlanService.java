package com.tjost.workouttracker.service;

import com.tjost.workouttracker.exception.PlanAlreadyExistsException;
import com.tjost.workouttracker.exception.PlanNotFoundException;
import com.tjost.workouttracker.model.WorkoutPlan;
import com.tjost.workouttracker.model.enums.Day;
import com.tjost.workouttracker.repository.WorkoutPlanRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutPlanService {

    private final WorkoutPlanRepository planRepo;

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
            WorkoutPlan updatedPlan
    ) {
        WorkoutPlan existingPlan = planRepo.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(planId));

        existingPlan.setDay(updatedPlan.getDay());
        existingPlan.setName(updatedPlan.getName());
        existingPlan.setComment(updatedPlan.getComment());

        return existingPlan;
    }

    @Transactional
    public void deleteWorkoutPlan(Long planId) {
      WorkoutPlan plan = planRepo.findById(planId)
              .orElseThrow(() -> new PlanNotFoundException(planId));
    planRepo.delete(plan);
    };


}
