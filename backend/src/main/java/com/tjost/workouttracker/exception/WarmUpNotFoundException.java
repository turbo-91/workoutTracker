package com.tjost.workouttracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WarmUpNotFoundException extends RuntimeException {
    public WarmUpNotFoundException(Long id) {
        super("WarmUp with Id " + id +" not found.");
    }
}
