package com.tjost.workouttracker.controller;

import com.tjost.workouttracker.dto.WorkoutLogRequest;
import com.tjost.workouttracker.model.WorkoutLog;
import com.tjost.workouttracker.service.WorkoutLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/workout-logs")
@RequiredArgsConstructor
public class WorkoutLogController {

    private final WorkoutLogService logService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkoutLog createWorkoutLog(@Valid @RequestBody
                                           WorkoutLogRequest request) {
        return logService.createWorkoutLog(
                request.planId(),
                request.workoutDate(),
                request.comment(),
                request.createdAt());
    }

    @GetMapping("/{id}")
    public WorkoutLog getWorkoutLogById(@PathVariable Long id) {
        return logService.getWorkoutLogById(id);
    }

    @GetMapping("/by-date/{workoutDate}")
    public WorkoutLog getWorkoutLogByWorkoutDate(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // GET /api/workout-logs/by-date/2026-08-19
            LocalDate workoutDate // Spring transforms ISO date format into LocalDate
    ) {
        return logService.getWorkoutLogByWorkoutDate(workoutDate);
    }

    @PutMapping("/{id}")
    public WorkoutLog updateWorkoutLog(
            @PathVariable Long id,
            @Valid @RequestBody WorkoutLogRequest log
    ) {
        return logService.updateWorkoutLog(id, log);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorkoutLog(@PathVariable Long id) {
        logService.deleteWorkoutLog(id);
    }

}
