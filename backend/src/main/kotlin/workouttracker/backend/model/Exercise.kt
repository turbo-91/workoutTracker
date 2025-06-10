package workouttracker.backend.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import workouttracker.backend.model.enums.MuscleGroup

@Document(collection = "exercise")
data class Exercise(@Id val id: String,
                    var name: String,
                    var muscleGroup: MuscleGroup,
                    var weight: String,
                    var sets: List<Int>,
                    var comment: String = "")
