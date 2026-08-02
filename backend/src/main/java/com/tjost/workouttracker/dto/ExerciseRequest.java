package com.tjost.workouttracker.dto;

import jakarta.validation.constraints.NotBlank;

public record ExerciseRequest(
        @NotBlank String name
) {
}