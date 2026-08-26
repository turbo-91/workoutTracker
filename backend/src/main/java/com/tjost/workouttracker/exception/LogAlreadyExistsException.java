package com.tjost.workouttracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;

@ResponseStatus(HttpStatus.CONFLICT)
public class LogAlreadyExistsException extends RuntimeException {
    public LogAlreadyExistsException(LocalDate workoutDate) {
        super("Log from the '" + workoutDate + "' already exists.");
    }
}
