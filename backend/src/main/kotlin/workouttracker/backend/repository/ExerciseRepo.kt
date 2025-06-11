package workouttracker.backend.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import workouttracker.backend.model.Exercise
import workouttracker.backend.model.enums.MuscleGroup

@Repository
interface ExerciseRepo : MongoRepository<Exercise, String> {
    fun findByName(name : String) : List<Exercise>
    fun findByMuscleGroup(muscleGroup: MuscleGroup): List<Exercise>
    fun existsByName(name: String): Boolean
}