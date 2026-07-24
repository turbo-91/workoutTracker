package com.tjost.workouttracker.repository;

import com.tjost.workouttracker.model.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {
    boolean existsByNameIgnoreCase(String name);

    WorkoutPlan findByPlanId(Long planId);
}
