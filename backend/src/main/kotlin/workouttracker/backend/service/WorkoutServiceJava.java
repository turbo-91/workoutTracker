package workouttracker.backend.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import workouttracker.backend.model.Exercise;
import workouttracker.backend.repository.ExerciseRepo;
import workouttracker.backend.repository.WorkoutLogRepo;
import workouttracker.backend.repository.WorkoutRepo;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class WorkoutServiceJava {
    private final ExerciseRepo exerciseRepo;
    private final WorkoutRepo workoutRepo;
    private WorkoutLogRepo workoutLogRepo;

    private static final Logger logger = LoggerFactory.getLogger(WorkoutServiceJava.class);

    // exercise management
    public Optional<Exercise> getExerciseById(String id) {
        return exerciseRepo.findById(id);
    }

    public Exercise createExercise(Exercise exercise) {
        exerciseRepo.save(exercise);
        return exercise;
    }

    // workout management
    // workoutlog management


}