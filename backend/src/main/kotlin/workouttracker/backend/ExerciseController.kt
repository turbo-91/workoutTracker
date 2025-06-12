package workouttracker.backend

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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

    @GetMapping("/id/{id}")
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

    @PostMapping
    fun createExercise(@RequestBody exercise: Exercise): ResponseEntity<Exercise> {
        return ResponseEntity.ok(exerciseService.createExercise(exercise))
    }

    @PutMapping("/{id}/update")
    fun updateExercise(
        @PathVariable id: String,
        @RequestBody exercise: Exercise
    ): ResponseEntity<Exercise> {
        val existing = exerciseService.getExerciseById(id)
            .orElseThrow { ExerciseNotFoundException("Exercise with id $id not found") }
        val updated = exerciseService.updateExercise(id, exercise)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun deleteExercise(@PathVariable id: String): ResponseEntity<Exercise> {
        val toDelete = exerciseService.getExerciseById(id)
            .orElseThrow { ExerciseNotFoundException("…") }
        exerciseService.deleteExercise(id)
        return ResponseEntity.ok(toDelete)
    }



}