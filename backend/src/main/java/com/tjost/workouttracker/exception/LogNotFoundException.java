package com.tjost.workouttracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LogNotFoundException extends RuntimeException {
    public LogNotFoundException(Long id) {
        super("Workout Log with Id " + id +" not found.");
    }
}
