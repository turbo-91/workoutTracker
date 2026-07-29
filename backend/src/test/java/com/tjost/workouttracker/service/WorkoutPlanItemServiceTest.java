package com.tjost.workouttracker.service;

import com.tjost.workouttracker.dto.WorkoutPlanItemRequest;
import com.tjost.workouttracker.exception.ExerciseNotFoundException;
import com.tjost.workouttracker.exception.ItemNotFoundException;
import com.tjost.workouttracker.exception.PlanNotFoundException;
import com.tjost.workouttracker.model.Exercise;
import com.tjost.workouttracker.model.WorkoutPlan;
import com.tjost.workouttracker.model.WorkoutPlanItem;
import com.tjost.workouttracker.model.enums.Day;
import com.tjost.workouttracker.repository.ExerciseRepository;
import com.tjost.workouttracker.repository.WorkoutPlanItemRepository;
import com.tjost.workouttracker.repository.WorkoutPlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutPlanItemServiceTest {

    @Mock
    private ExerciseRepository exerciseRepo;

    @Mock
    private WorkoutPlanRepository planRepo;

    @Mock
    private WorkoutPlanItemRepository itemRepo;

    @InjectMocks
    private WorkoutPlanItemService workoutPlanItemService;

    private WorkoutPlan workoutPlan;
    private Exercise exercise;
    private WorkoutPlanItem workoutPlanItem;

    @BeforeEach
    void setUp() {
        workoutPlan = WorkoutPlan.builder()
                .day(Day.DAY1)
                .name("Push Day")
                .comment("Chest, shoulders and triceps")
                .build();

        exercise = new Exercise("Bench Press");

        workoutPlanItem = WorkoutPlanItem.builder()
                .workoutPlan(workoutPlan)
                .exercise(exercise)
                .exercisePosition((short) 1)
                .targetWeightKg(new BigDecimal("80.00"))
                .targetRepMin((short) 8)
                .targetRepMax((short) 12)
                .comment("Control the movement")
                .build();
    }

    @Test
    void getWorkoutPlanItemById_shouldReturnItem_whenItemExists() {
        // GIVEN
        Long itemId = 1L;

        when(itemRepo.findById(itemId))
                .thenReturn(Optional.of(workoutPlanItem));

        // WHEN
        WorkoutPlanItem result =
                workoutPlanItemService.getWorkoutPlanItemById(itemId);

        // THEN
        assertSame(workoutPlanItem, result);
        verify(itemRepo).findById(itemId);
    }

    @Test
    void getWorkoutPlanItemById_shouldThrowException_whenItemNotFound() {
        // GIVEN
        Long itemId = 1L;

        when(itemRepo.findById(itemId))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(
                ItemNotFoundException.class,
                () -> workoutPlanItemService.getWorkoutPlanItemById(itemId)
        );

        verify(itemRepo).findById(itemId);
    }

    @Test
    void createWorkoutPlanItem_shouldSaveItem_whenPlanAndExerciseExist() {
        // GIVEN
        Long planId = 1L;
        Long exerciseId = 2L;
        short exercisePosition = 1;
        BigDecimal targetWeightKg = new BigDecimal("80.00");
        short targetRepMin = 8;
        short targetRepMax = 12;
        String comment = "Control the movement";

        when(planRepo.findById(planId))
                .thenReturn(Optional.of(workoutPlan));

        when(exerciseRepo.findById(exerciseId))
                .thenReturn(Optional.of(exercise));

        when(itemRepo.save(any(WorkoutPlanItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        WorkoutPlanItem result =
                workoutPlanItemService.createWorkoutPlanItem(
                        planId,
                        exerciseId,
                        exercisePosition,
                        targetWeightKg,
                        targetRepMin,
                        targetRepMax,
                        comment
                );

        // THEN
        assertNotNull(result);
        assertNull(result.getPlanItemId());
        assertSame(workoutPlan, result.getWorkoutPlan());
        assertSame(exercise, result.getExercise());
        assertEquals(exercisePosition, result.getExercisePosition());
        assertEquals(targetWeightKg, result.getTargetWeightKg());
        assertEquals(targetRepMin, result.getTargetRepMin());
        assertEquals(targetRepMax, result.getTargetRepMax());
        assertEquals(comment, result.getComment());

        verify(planRepo).findById(planId);
        verify(exerciseRepo).findById(exerciseId);
        verify(itemRepo).save(any(WorkoutPlanItem.class));
    }

    @Test
    void createWorkoutPlanItem_shouldThrowException_whenPlanNotFound() {
        // GIVEN
        Long planId = 1L;
        Long exerciseId = 2L;

        when(planRepo.findById(planId))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(
                PlanNotFoundException.class,
                () -> workoutPlanItemService.createWorkoutPlanItem(
                        planId,
                        exerciseId,
                        (short) 1,
                        new BigDecimal("80.00"),
                        (short) 8,
                        (short) 12,
                        "Control the movement"
                )
        );

        verify(planRepo).findById(planId);
        verify(exerciseRepo, never()).findById(anyLong());
        verify(itemRepo, never()).save(any(WorkoutPlanItem.class));
    }

    @Test
    void createWorkoutPlanItem_shouldThrowException_whenExerciseNotFound() {
        // GIVEN
        Long planId = 1L;
        Long exerciseId = 2L;

        when(planRepo.findById(planId))
                .thenReturn(Optional.of(workoutPlan));

        when(exerciseRepo.findById(exerciseId))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(
                ExerciseNotFoundException.class,
                () -> workoutPlanItemService.createWorkoutPlanItem(
                        planId,
                        exerciseId,
                        (short) 1,
                        new BigDecimal("80.00"),
                        (short) 8,
                        (short) 12,
                        "Control the movement"
                )
        );

        verify(planRepo).findById(planId);
        verify(exerciseRepo).findById(exerciseId);
        verify(itemRepo, never()).save(any(WorkoutPlanItem.class));
    }

    @Test
    void updateWorkoutPlanItem_shouldUpdateItem_whenItemExists() {
        // GIVEN
        Long itemId = 1L;

        WorkoutPlanItemRequest request =
                mock(WorkoutPlanItemRequest.class);

        when(request.exercisePosition()).thenReturn((short) 2);
        when(request.targetWeightKg()).thenReturn(new BigDecimal("90.00"));
        when(request.targetRepMin()).thenReturn((short) 6);
        when(request.targetRepMax()).thenReturn((short) 10);
        when(request.comment()).thenReturn("Increase weight gradually");

        when(itemRepo.findById(itemId))
                .thenReturn(Optional.of(workoutPlanItem));

        // WHEN
        WorkoutPlanItem result =
                workoutPlanItemService.updateWorkoutPlanItem(itemId, request);

        // THEN
        assertSame(workoutPlanItem, result);
        assertEquals((short) 2, result.getExercisePosition());
        assertEquals(
                new BigDecimal("90.00"),
                result.getTargetWeightKg()
        );
        assertEquals((short) 6, result.getTargetRepMin());
        assertEquals((short) 10, result.getTargetRepMax());
        assertEquals(
                "Increase weight gradually",
                result.getComment()
        );

        verify(itemRepo).findById(itemId);
        verify(itemRepo, never()).save(any(WorkoutPlanItem.class));
    }

    @Test
    void updateWorkoutPlanItem_shouldThrowException_whenItemNotFound() {
        // GIVEN
        Long itemId = 1L;

        WorkoutPlanItemRequest request =
                mock(WorkoutPlanItemRequest.class);

        when(itemRepo.findById(itemId))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(
                ItemNotFoundException.class,
                () -> workoutPlanItemService.updateWorkoutPlanItem(
                        itemId,
                        request
                )
        );

        verify(itemRepo).findById(itemId);
        verifyNoInteractions(request);
        verify(itemRepo, never()).save(any(WorkoutPlanItem.class));
    }

    @Test
    void deleteWorkoutPlanItem_shouldDeleteItem_whenItemExists() {
        // GIVEN
        Long itemId = 1L;

        when(itemRepo.findById(itemId))
                .thenReturn(Optional.of(workoutPlanItem));

        // WHEN
        workoutPlanItemService.deleteWorkoutPlanItem(itemId);

        // THEN
        verify(itemRepo).findById(itemId);
        verify(itemRepo).delete(workoutPlanItem);
    }

    @Test
    void deleteWorkoutPlanItem_shouldThrowException_whenItemNotFound() {
        // GIVEN
        Long itemId = 1L;

        when(itemRepo.findById(itemId))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(
                ItemNotFoundException.class,
                () -> workoutPlanItemService.deleteWorkoutPlanItem(itemId)
        );

        verify(itemRepo).findById(itemId);
        verify(itemRepo, never()).delete(any(WorkoutPlanItem.class));
    }
}