package com.tjost.workouttracker.dto;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record WorkoutPlanItemRequest(@NotNull Long planId,
                                     @NotNull Long exerciseId,
                                     @NotNull short exercisePosition,
                                     @NotNull BigDecimal targetWeightKg,
                                     @NotNull short targetRepMin,
                                     @NotNull short targetRepMax,
                                     String comment) {
}
