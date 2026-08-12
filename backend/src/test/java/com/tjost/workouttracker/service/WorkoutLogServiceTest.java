package com.tjost.workouttracker.service;

import com.tjost.workouttracker.model.WorkoutLog;
import com.tjost.workouttracker.model.WorkoutPlan;
import com.tjost.workouttracker.model.enums.Day;
import com.tjost.workouttracker.repository.WorkoutLogRepository;
import com.tjost.workouttracker.repository.WorkoutPlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@ExtendWith(MockitoExtension.class)
public class WorkoutLogServiceTest {

    @Mock
    private WorkoutPlanRepository planRepo;

    @Mock
    private WorkoutLogRepository logRepo;

    @InjectMocks
    private WorkoutLogService logService;

    private WorkoutPlan workoutPlan;
    private WorkoutLog workoutLog;

    @BeforeEach
    void setUp() {
        workoutPlan = WorkoutPlan.builder()
                .day(Day.DAY1)
                .name("Push Day")
                .comment("Chest, shoulders and triceps")
                .build();

        workoutLog = WorkoutLog.builder()
                .workoutPlan(workoutPlan)
                .workoutDate(LocalDate.of(2026, 8, 11))
                .comment("Good job today!")
                .createdAt(OffsetDateTime.now())
                .build();
    }

    @Test
    void getWorkoutLogById_shouldReturnLog_whenItemExsists() {}
}
