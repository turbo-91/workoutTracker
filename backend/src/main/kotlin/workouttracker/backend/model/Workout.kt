package workouttracker.backend.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "workout")
data class Workout(@Id val id: String,
                   var name: String,
                   var exercises: List<Exercise>,
                   var comment: String = "")
