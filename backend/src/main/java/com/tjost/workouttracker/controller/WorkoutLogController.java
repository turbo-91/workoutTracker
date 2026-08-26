package com.tjost.workouttracker.controller;

import com.tjost.workouttracker.dto.WorkoutLogRequest;
import com.tjost.workouttracker.dto.WorkoutLogResponse;
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
    public WorkoutLogResponse createWorkoutLog(@Valid @RequestBody
                                           WorkoutLogRequest request) {
        WorkoutLog log = logService.createWorkoutLog(
                request.planId(),
                request.workoutDate(),
                request.comment(),
                request.createdAt()
        );
        return toResponse(log);
    }

    @GetMapping("/{id}")
    public WorkoutLogResponse getWorkoutLogById(@PathVariable Long id) {
        WorkoutLog log = logService.getWorkoutLogById(id);
        return toResponse(log);
    }

    @GetMapping("/by-date/{workoutDate}")
    public WorkoutLogResponse getWorkoutLogByWorkoutDate(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // GET /api/workout-logs/by-date/2026-08-19
            LocalDate workoutDate // Spring transforms ISO date format into LocalDate
    ) {
        WorkoutLog log = logService.getWorkoutLogByWorkoutDate(workoutDate);
        return toResponse(log);
    }

    @PutMapping("/{id}")
    public WorkoutLogResponse updateWorkoutLog(
            @PathVariable Long id,
            @Valid @RequestBody WorkoutLogRequest request
    ) {
        WorkoutLog log = logService.updateWorkoutLog(id, request);

        return toResponse(log);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorkoutLog(@PathVariable Long id) {
        logService.deleteWorkoutLog(id);
    }

    private WorkoutLogResponse toResponse(WorkoutLog log) {
        return new WorkoutLogResponse(
                log.getLogId(),
                log.getWorkoutPlan().getPlanId(),
                log.getWorkoutDate(),
                log.getComment(),
                log.getCreatedAt()
        );
    }

}
