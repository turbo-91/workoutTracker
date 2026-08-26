package com.tjost.workouttracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LogNotFoundExceptionWorkoutDate extends RuntimeException {
    public LogNotFoundExceptionWorkoutDate(LocalDate workoutDate) {
        super("Workout Log from " + workoutDate+" not found.");
    }
}
