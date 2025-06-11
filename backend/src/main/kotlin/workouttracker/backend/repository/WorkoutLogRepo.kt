package workouttracker.backend.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import workouttracker.backend.model.WorkoutLog
import workouttracker.backend.model.enums.Mon
import workouttracker.backend.model.enums.TrainingGoal
import java.time.LocalDateTime
import java.time.Month
import java.util.*

@Repository
interface WorkoutLogRepo : MongoRepository<WorkoutLog, String> {
    fun findByCreatedAt(createdAt: LocalDateTime): List<WorkoutLog>
    fun existsByCreatedAt(createdAt: LocalDateTime): Boolean;
    fun findByTrainingGoal(trainingGoal: TrainingGoal): List<WorkoutLog>
    fun findByWorkoutId(workoutId: String): List<WorkoutLog>
    fun deleteWorkoutLogById(workoutId: String);
    fun findTopByWorkoutIdOrderByCreatedAtDesc(workoutId: String): Optional<WorkoutLog>;
    fun findWorkoutLogsByMon(mon: Mon): List<WorkoutLog>
    fun findFiveLatestWorkoutLogs(date: LocalDateTime): List<WorkoutLog>
    fun findByCreatedAtBetween(startDate: LocalDateTime, endDate: LocalDateTime): List<WorkoutLog>
}