package com.tjost.workouttracker.repository;

import com.tjost.workouttracker.model.WorkoutLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface WorkoutLogRepository extends JpaRepository<WorkoutLog, Long> {
    Optional<WorkoutLog> findByWorkoutDate(LocalDate WorkoutDate);
}
