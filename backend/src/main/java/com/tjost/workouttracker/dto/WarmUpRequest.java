package com.tjost.workouttracker.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record WarmUpRequest(@NotNull Long planItemId,
                            @NotNull short setNumber,
                            @NotNull BigDecimal weightKg,
                            @NotNull short repMin,
                            @NotNull short repMax,
                            @NotNull short restSeconds) {
}
