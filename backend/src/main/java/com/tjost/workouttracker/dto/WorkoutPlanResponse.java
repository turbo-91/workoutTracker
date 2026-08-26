package com.tjost.workouttracker.dto;

import com.tjost.workouttracker.model.enums.Day;

public record WorkoutPlanResponse(
        Long planId,
        Day day,
        String name,
        String comment
) {}