package com.tjost.workouttracker.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record WorkoutLogResponse(
        Long logId,
        Long planId,
        LocalDate workoutDate,
        String comment,
        OffsetDateTime createdAt
) {
}
