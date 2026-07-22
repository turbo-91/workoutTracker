package com.tjost.workouttracker.exception;

public class ExerciseAlreadyExistsException extends RuntimeException {
    public ExerciseAlreadyExistsException(String name) {
        super("Exercise : " + name + "already exists.");
    }
}
