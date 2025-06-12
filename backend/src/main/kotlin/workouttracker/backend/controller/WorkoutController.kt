package workouttracker.backend.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import workouttracker.backend.exception.WorkoutLogNotFoundException
import workouttracker.backend.exception.WorkoutNotFoundException
import workouttracker.backend.model.Workout
import workouttracker.backend.service.WorkoutService

@RestController
@RequestMapping("/api/workout")
class WorkoutController (private val workoutService: WorkoutService) {

    @GetMapping
    fun getAllWorkouts() : ResponseEntity<List<Workout>> {
        return ResponseEntity.ok(workoutService.getAllWorkouts())
    }

    @GetMapping("/{id}")
    fun getWorkoutById(@PathVariable("id") id : String) : ResponseEntity<Workout> {
        val workout = workoutService
            .getWorkoutById(id)
            .orElseThrow {WorkoutLogNotFoundException("Workout with id: $id not found")}
        return ResponseEntity.ok(workout)
    }

    @GetMapping("/{name}")
    fun getWorkoutByName(@PathVariable("name") name : String) : ResponseEntity<Workout> {
        val workout = workoutService
            .getWorkoutByName(name)
            .orElseThrow{WorkoutLogNotFoundException("Workout with name: $name")}
        return ResponseEntity.ok(workout)
    }

    @PostMapping("/createWorkout")
    fun createWorkout(@RequestBody workout: Workout) : ResponseEntity<Workout> {
        return ResponseEntity.ok(workoutService.createWorkout(workout))
    }

    @PutMapping("/{id}/update")
    fun updateWorkout(@RequestBody id: String, workout: Workout) : ResponseEntity<Workout> {
        val existingWorkout = workoutService
            .getWorkoutById(id)
            .orElseThrow{WorkoutNotFoundException("Workout with id: $id not found")}
        val updated = workoutService.updateWorkout(id, workout)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}/delete")
    fun deleteWorkout(@PathVariable("id") id : String) : ResponseEntity<Workout> {
        val toDelete = workoutService.getWorkoutById(id).
        orElseThrow { WorkoutNotFoundException("Workout with id: $id not found")}
        workoutService.deleteWorkout((id))
        return ResponseEntity.ok(toDelete)
    }
}