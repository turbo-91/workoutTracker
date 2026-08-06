package com.tjost.workouttracker.repository;

import com.tjost.workouttracker.dto.WorkoutPlanItemDetailsDTO;
import com.tjost.workouttracker.model.WorkoutPlanItem;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface WorkoutPlanItemRepository extends JpaRepository<WorkoutPlanItem, Long> {
    List<WorkoutPlanItem> findAllByWorkoutPlan_PlanIdOrderByExercisePositionAsc(Long planId);
}
