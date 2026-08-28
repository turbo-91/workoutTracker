package com.tjost.workouttracker.repository;

import com.tjost.workouttracker.model.WarmUp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface WarmUpRepository extends JpaRepository<WarmUp, Long> {
    boolean existsByWorkoutPlanItemId(Long planItemId);
    Optional<WarmUp> findByWorkoutPlanItemId(Long planItemId);
}
