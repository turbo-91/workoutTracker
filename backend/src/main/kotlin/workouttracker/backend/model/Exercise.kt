package workouttracker.backend.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import workouttracker.backend.model.enums.MuscleGroup

@Document(collection = "exercise")
data class Exercise(@Id val id: String,
                    val name: String,
                    val muscleGroup: MuscleGroup,
                    val weight: String,
                    val sets: List<Int>,
                    val comment: String = "")
