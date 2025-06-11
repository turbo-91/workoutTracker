package workouttracker.backend.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import workouttracker.backend.model.Exercise
import workouttracker.backend.model.Workout
import workouttracker.backend.repository.ExerciseRepo
import workouttracker.backend.repository.WorkoutLogRepo
import workouttracker.backend.repository.WorkoutRepo
import java.util.Optional

@Service
class WorkoutService(
    private val exerciseRepo: ExerciseRepo,
    private val workoutRepo: WorkoutRepo
) {
    private lateinit var workoutLogRepo: WorkoutLogRepo

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(WorkoutService::class.java)
    }

    // exercise management

    fun getAllExercises(): List<Exercise> {
        return exerciseRepo.findAll();
    }

    fun getExerciseById(id: String): Optional<Exercise> {
        return exerciseRepo.findById(id)
    }

    fun getExerciseByName(name: String): List<Exercise> {
        if (!exerciseRepo.existsByName(name)) {
            throw NoSuchElementException("No exercise with name $name"); // implement custom exception management later
        }
        return exerciseRepo.findByName(name)
    }

    fun createExercise(exercise: Exercise): Exercise {
        exerciseRepo.save(exercise)
        return exercise
    }

    fun updateExercise(id: String, updated: Exercise): Exercise {
        val existing = exerciseRepo.findById(id)
            .orElseThrow{NoSuchElementException("Exercise with id: $id not found")};
        existing.name = updated.name;
        existing.muscleGroup = updated.muscleGroup;
        existing.weight = updated.weight;
        existing.sets = updated.sets;
        existing.comment = updated.comment;
        return exerciseRepo.save(existing);
    }

    fun deleteExercise(id: String) {
        if (!exerciseRepo.existsById(id)) {
            throw NoSuchElementException("No exercise with id $id"); // implement custom exception management later
        }
        exerciseRepo.deleteById(id);
    }

    // workout management

    fun getWorkoutById(id: String): Optional<Workout> {
        return workoutRepo.findById(id);
    }

    fun getAllWorkouts(): List<Workout> {
        return workoutRepo.findAll();
    }

    fun getWorkoutByName(name: String): List<Workout> {
        if (!exerciseRepo.existsByName(name)) {
            throw NoSuchElementException("No exercise with name $name"); // implement custom exception management later
        }
        return workoutRepo.findByName(name)
    }

    fun createWorkout(workout: Workout): Workout {
        return workoutRepo.save(workout);
    }

    fun updateWorkout(id: String, updated: Workout): Workout {
        val existing = workoutRepo.findById(id)
            .orElseThrow{NoSuchElementException("Workout with id: $id not found")};
        existing.name = updated.name;
        existing.exercises = updated.exercises;
        existing.comment = updated.comment;
        return workoutRepo.save(existing);
    }

    fun deleteWorkout(id: String) {
        if (!exerciseRepo.existsById(id)) {
            throw NoSuchElementException("No exercise with id: $id");
        }
        return workoutRepo.deleteById(id);
    }

    // workoutlog management
}
