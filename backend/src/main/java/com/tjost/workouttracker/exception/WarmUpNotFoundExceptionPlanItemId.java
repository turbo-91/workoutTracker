package com.tjost.workouttracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WarmUpNotFoundExceptionPlanItemId extends RuntimeException {
    public WarmUpNotFoundExceptionPlanItemId(Long id) {
        super("WarmUp for WorkoutPlanItem with Id " + id +" not found.");
    }
}
