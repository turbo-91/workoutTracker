package com.tjost.workouttracker.service;

import org.springframework.transaction.annotation.Transactional;
import com.tjost.workouttracker.exception.ExerciseAlreadyExistsException;
import com.tjost.workouttracker.exception.ExerciseNotFoundException;
import com.tjost.workouttracker.model.Exercise;
import com.tjost.workouttracker.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private static final Logger logger = LoggerFactory.getLogger(ExerciseService.class);
    private final ExerciseRepository exerciseRepo;

    public List<Exercise> getAllExercises() {
        return exerciseRepo.findAll();
    }

    public Exercise getExerciseById(Long exerciseId) {
        return exerciseRepo.findById(exerciseId)
                .orElseThrow(() -> new ExerciseNotFoundException(exerciseId));
    }

    public Exercise createExercise(String name) {
        if (exerciseRepo.existsByNameIgnoreCase(name)) {
            throw new ExerciseAlreadyExistsException(name);
        }

        return exerciseRepo.save(new Exercise(name));
    }

    @Transactional
    public Exercise updateExercise(Long exerciseId, String newName) {
        Exercise exercise = exerciseRepo.findById(exerciseId)
                .orElseThrow(() -> new ExerciseNotFoundException(exerciseId));
        exercise.setName(newName);
        return exercise;
    }

    @Transactional
    public void deleteExercise(Long exerciseId) {
        Exercise exercise = exerciseRepo.findById(exerciseId)
                .orElseThrow(() ->
                        new ExerciseNotFoundException(exerciseId));

        exerciseRepo.delete(exercise);
    }
}

