package workouttracker.backend.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import workouttracker.backend.model.enums.TrainingGoal
import java.time.LocalDateTime

@Document(collection = "workoutLog")
data class WorkoutLog(
    @Id val id: String,
    @CreatedDate
    var createdAt: LocalDateTime? = null,
    var trainingGoal: TrainingGoal,
    var workout: Workout,
    var comment: String = ""
)
