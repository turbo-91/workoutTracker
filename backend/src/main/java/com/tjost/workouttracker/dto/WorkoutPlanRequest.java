package com.tjost.workouttracker.dto;

import com.tjost.workouttracker.model.enums.Day;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WorkoutPlanRequest(
        @NotNull Day day,
        @NotBlank String name,
        String comment
) {
}