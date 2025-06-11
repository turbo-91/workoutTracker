package workouttracker.backend.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import workouttracker.backend.model.Exercise
import workouttracker.backend.repository.ExerciseRepo
import java.util.*
import kotlin.NoSuchElementException

class ExerciseService(
    private val exerciseRepo: ExerciseRepo) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(WorkoutService::class.java)
    }

    fun getAllExercises(): List<Exercise> {
        return exerciseRepo.findAll();
    }

    fun getExerciseById(id: String): Optional<Exercise> {
        return exerciseRepo.findById(id)
    }

    fun getExerciseByName(name: String): Optional<Exercise> {
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
}
