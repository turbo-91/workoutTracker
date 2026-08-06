package com.tjost.workouttracker.dto;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record WorkoutPlanItemDetailsDTO(@NotNull short exercisePosition,
                                        @NotNull String exerciseName,
                                        @NotNull BigDecimal targetWeightKg,
                                        @NotNull short targetRepMin,
                                        @NotNull short targetRepMax,
                                        String comment) {
}
