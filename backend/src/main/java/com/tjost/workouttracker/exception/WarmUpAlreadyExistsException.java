package com.tjost.workouttracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;

@ResponseStatus(HttpStatus.CONFLICT)
public class WarmUpAlreadyExistsException extends RuntimeException {
    public WarmUpAlreadyExistsException(Long planItemId) {
        super("Warm Up for the workout plan item with the id " + planItemId + " already exists.");
    }
}
