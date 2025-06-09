package workouttracker.backend.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import workouttracker.backend.model.Exercise
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

    fun createExercise(exercise: Exercise): Exercise {
        exerciseRepo.save(exercise)
        return exercise
    }

    // tbd
//    fun updateExercise(id: String, updated: Exercise): Exercise {
//        return exercise;
//    }

    fun deleteExercise(id: String) {
        if (!exerciseRepo.existsById(id)) {
            throw NoSuchElementException("No exercise with id $id"); // implement custom exception management later
        }
        exerciseRepo.deleteById(id);
    }




    // workout management

    // workoutlog management
}
