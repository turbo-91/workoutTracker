package com.tjost.workouttracker.repository;

import com.tjost.workouttracker.model.WorkoutPlan;
import com.tjost.workouttracker.model.WorkoutPlanItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutPlanItemRepository extends JpaRepository<WorkoutPlanItem, Long> {
    boolean existsByWorkoutPlan_PlanIdAndExercisePosition(
            Long planId,
            int exercisePosition
    );
}
