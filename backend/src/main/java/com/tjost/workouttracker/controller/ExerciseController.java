package com.tjost.workouttracker.controller;

import com.tjost.workouttracker.dto.ExerciseRequest;
import com.tjost.workouttracker.model.Exercise;
import com.tjost.workouttracker.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Exercise createExercise(@RequestBody ExerciseRequest exercise) {
        return exerciseService.createExercise(exercise.name());
    }

    @GetMapping
    public List<Exercise> getAllExercises() {
        return exerciseService.getAllExercises();
    }

    @GetMapping("/{id}")
    public Exercise getExerciseById(@PathVariable Long id) {
        return exerciseService.getExerciseById(id);
    }

    @PutMapping("/{id}")
    public Exercise updateExercise(
            @PathVariable Long id,
            @RequestBody ExerciseRequest exercise
    ) {
        return exerciseService.updateExercise(id, exercise.name());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExercise(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
    }
}