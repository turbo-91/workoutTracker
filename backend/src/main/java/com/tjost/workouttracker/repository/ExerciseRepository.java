package com.tjost.workouttracker.repository;

import com.tjost.workouttracker.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository
        extends JpaRepository<Exercise, Long> {
}
