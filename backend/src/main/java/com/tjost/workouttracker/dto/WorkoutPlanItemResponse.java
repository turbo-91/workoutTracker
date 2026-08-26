package com.tjost.workouttracker.dto;

import java.math.BigDecimal;

public record WorkoutPlanItemResponse(
        Long planItemId,
        Long planId,
        Long exerciseId,
        short exercisePosition,
        BigDecimal targetWeightKg,
        short targetRepMin,
        short targetRepMax,
        String comment
        ) {
}
