package workouttracker.backend.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import workouttracker.backend.model.WorkoutLog
import workouttracker.backend.model.enums.TrainingGoal
import java.time.LocalDateTime

@Repository
interface WorkoutLogRepo : MongoRepository<WorkoutLog, String> {
    fun findByCreatedAt(createdAt: LocalDateTime): MutableList<WorkoutLog>
    fun findByTrainingGoal(trainingGoal: TrainingGoal): List<WorkoutLog>
}