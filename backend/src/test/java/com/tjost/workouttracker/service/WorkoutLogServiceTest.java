package com.tjost.workouttracker.service;

import com.tjost.workouttracker.dto.WorkoutLogRequest;
import com.tjost.workouttracker.exception.LogNotFoundException;
import com.tjost.workouttracker.exception.LogNotFoundExceptionWorkoutDate;
import com.tjost.workouttracker.exception.PlanNotFoundException;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    void getWorkoutLogById_shouldReturnLog_whenLogExists() {
        // GIVEN
        Long logId = 1L;
        when(logRepo.findById(logId)).
                thenReturn(Optional.of(workoutLog));

        // WHEN
        WorkoutLog result =
                logService.getWorkoutLogById(logId);

        // THEN
        assertSame(workoutLog, result);
        verify(logRepo).findById(logId);
    }

    @Test
    void getWorkoutLogById_ShouldThrowException_whenLogNotFound() {
        // GIVEN
        Long logId = 1L;

        when(logRepo.findById(logId)).
                thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(
                LogNotFoundException.class,
                () -> logService.getWorkoutLogById(logId)
        );

        verify(logRepo).findById(logId);
    }

    @Test
    void getWorkOutLogByWorkoutDate_ShouldReturnLog_whenLogExists() {
        // GIVEN
        LocalDate workoutDate = LocalDate.of(2026, 8, 18);

        when(logRepo.findByWorkoutDate(workoutDate))
                .thenReturn(Optional.of(workoutLog));

        // WHEN
        WorkoutLog result = logService.getWorkoutLogByWorkoutDate(workoutDate);

        // THEN
        assertSame(workoutLog, result);
        verify(logRepo).findByWorkoutDate(workoutDate);
    }

    @Test
    void getWorkoutLogById_shouldThrowException_whenLogNotFound() {
        // GIVEN
        LocalDate workoutDate = LocalDate.of(2026, 8, 19);

        when(logRepo.findByWorkoutDate(workoutDate))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(
                LogNotFoundExceptionWorkoutDate.class,
                () -> logService.getWorkoutLogByWorkoutDate(workoutDate)
        );

        verify(logRepo).findByWorkoutDate(workoutDate);
    }

    @Test
    void createWorkoutLog_shouldSaveLog_whenPlanExists() {
        // GIVEN
        Long planId = 1L;
        LocalDate workoutDate = LocalDate.of(2026, 8, 20);
        String comment = "Remember the extra set of crunches";
        OffsetDateTime createdAt = OffsetDateTime.now();

        when(planRepo.findById(planId))
                .thenReturn(Optional.of(workoutPlan));

        when(logRepo.save(any(WorkoutLog.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        WorkoutLog result = logService.createWorkoutLog(
                planId,
                workoutDate,
                comment,
                createdAt);

        // THEN
        assertNotNull(result);
        assertNull(result.getLogId());
        assertSame(workoutPlan, result.getWorkoutPlan());
        assertEquals(workoutDate, result.getWorkoutDate());
        assertEquals(comment, result.getComment());
        assertEquals(createdAt, result.getCreatedAt());

        verify(planRepo).findById(planId);
        verify(logRepo).save(any(WorkoutLog.class));
    }

    @Test
    void createWorkoutLog_shouldThrowException_whenPlanNotFound() {
        // GIVEN
        Long planId = 1L;

        when(planRepo.findById(planId))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(
                PlanNotFoundException.class,
                () -> logService.createWorkoutLog(
                        planId,
                        LocalDate.of(2026, 8, 21),
                        "Remember the extra set of crunches",
                        OffsetDateTime.now()
                )
        );

        verify(planRepo).findById(planId);
        verify(logRepo, never()).save(any(WorkoutLog.class));
    }

    @Test
    void updateWorkoutLog_shouldUpdateLog_whenLogExists() {
        // Given
        Long logId = 1L;
        OffsetDateTime createdAt = OffsetDateTime.now().minusDays(1);
        WorkoutLogRequest request =
                mock(WorkoutLogRequest.class);

        when(request.workoutDate()).thenReturn(LocalDate.of(2026, 8, 23));
        when(request.comment()).thenReturn("Push that extra rep!");
        when(request.createdAt()).thenReturn(createdAt);

        when(logRepo.findById(logId))
                .thenReturn(Optional.of(workoutLog));

        // WHEN
        WorkoutLog result =
                logService.updateWorkoutLog(logId, request);

        // THEN
        assertSame(workoutLog, result);
        assertEquals(LocalDate.of(2026, 8, 23), result.getWorkoutDate());
        assertEquals("Push that extra rep!", result.getComment());
        assertEquals(createdAt, result.getCreatedAt());

        verify(logRepo).findById(logId);
        verify(logRepo, never()).save(any(WorkoutLog.class));
    }

    @Test
    void updateWorkoutLog_shouldThrowException_whenLogNotFound() {
        // GIVEN
        Long logId = 1L;

        WorkoutLogRequest request =
                mock(WorkoutLogRequest.class);

        when(logRepo.findById(logId))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(
                LogNotFoundException.class,
                () -> logService.updateWorkoutLog(
                        logId,
                        request
                )
        );

        verify(logRepo).findById(logId);
        verifyNoInteractions(request);
        verify(logRepo, never()).save(any(WorkoutLog.class));
    }

    @Test
    void deleteWorkoutLog_ShouldDelete_Log_whenLogExists() {
        // GIVEN
        Long logId = 1L;

        when(logRepo.findById(logId))
                .thenReturn(Optional.of(workoutLog));

        // WHEN
        logService.deleteWorkoutLog(logId);

        // THEN
        verify(logRepo).findById(logId);
        verify(logRepo).delete(workoutLog);
    }

    @Test
    void deleteWorkoutLog_shouldThrowException_whenLogNotFound() {
        // GIVEN
        Long logId = 1L;

        when(logRepo.findById(logId))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(
                LogNotFoundException.class,
                () -> logService.deleteWorkoutLog(logId)
        );

        verify(logRepo).findById(logId);
        verify(logRepo, never()).delete(any(WorkoutLog.class));
    }


}
