package workouttracker.backend.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import workouttracker.backend.model.Workout
import java.util.*

@Repository
interface WorkoutRepo : MongoRepository<Workout, String> {
    fun findByName(name : String) : Optional<Workout>
    fun existsByName(name: String): Boolean
}