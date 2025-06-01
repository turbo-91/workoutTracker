package workouttracker.backend.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "workout")
data class Workout(@Id val id: String,
                   val name: String,
                   val exercises: List<Exercise>,
                   val comment: String = "")
