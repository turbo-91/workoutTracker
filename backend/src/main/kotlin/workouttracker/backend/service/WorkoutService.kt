package workouttracker.backend.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import workouttracker.backend.exception.WorkoutNotFoundException
import workouttracker.backend.model.Exercise
import workouttracker.backend.model.Workout
import workouttracker.backend.model.WorkoutLog
import workouttracker.backend.model.enums.TrainingGoal
import workouttracker.backend.repository.ExerciseRepo
import workouttracker.backend.repository.WorkoutLogRepo
import workouttracker.backend.repository.WorkoutRepo
import java.time.LocalDateTime
import java.util.Optional

@Service
class WorkoutService(
    private val workoutRepo: WorkoutRepo,
    private val workoutLogRepo: WorkoutLogRepo
) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(WorkoutService::class.java)
    }

    fun getWorkoutById(id: String): Optional<Workout> {
        return workoutRepo.findById(id);
    }

    fun getAllWorkouts(): List<Workout> {
        return workoutRepo.findAll();
    }

    fun getWorkoutByName(name: String): Optional<Workout> {
        if (!workoutRepo.existsByName(name)) {
            throw WorkoutNotFoundException("No workout with name $name"); // implement custom exception management later
        }
        return workoutRepo.findByName(name)
    }

    fun createWorkout(workout: Workout): Workout {
        return workoutRepo.save(workout);
    }

    fun updateWorkout(id: String, updated: Workout): Workout {
        val existing = workoutRepo.findById(id)
            .orElseThrow{WorkoutNotFoundException("Workout with id: $id not found")};
        existing.name = updated.name;
        existing.exercises = updated.exercises;
        existing.comment = updated.comment;
        return workoutRepo.save(existing);
    }

    fun deleteWorkout(id: String) {
        if (!workoutRepo.existsById(id)) {
            throw WorkoutNotFoundException("No workout with id: $id");
        }
        return workoutRepo.deleteById(id);
    }
}
