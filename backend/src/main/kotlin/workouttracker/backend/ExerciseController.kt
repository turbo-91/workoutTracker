package workouttracker.backend

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import workouttracker.backend.exception.ExerciseNotFoundException
import workouttracker.backend.model.Exercise
import workouttracker.backend.service.ExerciseService
import java.util.*

@RestController
@RequestMapping("/exercise")
class ExerciseController(private val exerciseService: ExerciseService) {

    @GetMapping
    fun getExercises(): ResponseEntity<List<Exercise>> {
        return ResponseEntity.ok(exerciseService.getAllExercises())
    }

    @GetMapping
    fun getExerciseById(@PathVariable id: String): ResponseEntity<Exercise> {
        val exercise = exerciseService
            .getExerciseById(id)
            .orElseThrow {ExerciseNotFoundException("Exercise with id $id not found") }

        return ResponseEntity.ok(exercise)
    }

    @GetMapping("/name/{name}")
    fun getExerciseByName(
        @PathVariable name: String
    ): ResponseEntity<Exercise> {
        val exercise = exerciseService
            .getExerciseByName(name)
            .orElseThrow { ExerciseNotFoundException("Exercise with name \"$name\" not found") }
        return ResponseEntity.ok(exercise)
    }

}