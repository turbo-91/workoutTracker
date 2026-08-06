package com.tjost.workouttracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ItemNotFoundExceptionPlanId extends RuntimeException {
    public ItemNotFoundExceptionPlanId(Long planId) {
        super("Workout plan item for plan with Id " + planId +" not found.");
    }
}
