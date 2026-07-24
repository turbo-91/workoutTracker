package com.tjost.workouttracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PlanAlreadyExistsException extends RuntimeException {
    public PlanAlreadyExistsException(String name) {
        super("Plan: '" + name + "' already exists.");
    }
}
