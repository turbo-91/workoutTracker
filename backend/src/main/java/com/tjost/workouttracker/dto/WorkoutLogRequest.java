package com.tjost.workouttracker.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record WorkoutLogRequest(@NotNull Long planId,
                                @NotNull LocalDate workoutDate,
                                String comment,
                                @NotNull OffsetDateTime createdAt) {

}
