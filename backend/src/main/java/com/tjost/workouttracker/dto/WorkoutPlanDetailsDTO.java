package com.tjost.workouttracker.dto;

import com.tjost.workouttracker.model.enums.Day;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record WorkoutPlanDetailsDTO(
        @NotNull Day day,
        @NotBlank String name,
        @NotBlank List<WorkoutPlanItemDetailsDTO> items,
        String comment
) {
}