package com.tjost.workouttracker.service;

import com.tjost.workouttracker.dto.WorkoutPlanDetailsDTO;
import com.tjost.workouttracker.dto.WorkoutPlanItemDetailsDTO;
import com.tjost.workouttracker.dto.WorkoutPlanRequest;
import com.tjost.workouttracker.exception.PlanAlreadyExistsException;
import com.tjost.workouttracker.exception.PlanNotFoundException;
import com.tjost.workouttracker.model.Exercise;
import com.tjost.workouttracker.model.WorkoutPlan;
import com.tjost.workouttracker.model.WorkoutPlanItem;
import com.tjost.workouttracker.model.enums.Day;
import com.tjost.workouttracker.repository.WorkoutPlanItemRepository;
import com.tjost.workouttracker.repository.WorkoutPlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutPlanServiceTest {

    @Mock
    private WorkoutPlanRepository planRepo;
    @Mock
    private WorkoutPlanItemRepository itemRepo;

    @InjectMocks
    private WorkoutPlanService workoutPlanService;

    private WorkoutPlan workoutPlan;


    @BeforeEach
    void setUp() {
        workoutPlan = WorkoutPlan.builder()
                .day(Day.DAY1)
                .name("Push Day")
                .comment("Chest, shoulders and triceps")
                .build();
    }

    @Test
    void getAllWorkoutPlans_shouldReturnListOfWorkoutPlans() {
        // GIVEN
        List<WorkoutPlan> mockPlans = List.of(workoutPlan);
        when(planRepo.findAll()).thenReturn(mockPlans);

        // WHEN
        List<WorkoutPlan> result =
                workoutPlanService.getAllWorkoutPlans();

        // THEN
        assertEquals(mockPlans, result);
        assertEquals(1, result.size());
        verify(planRepo).findAll();
    }

    @Test
    void getAllWorkoutPlans_shouldReturnEmptyList_whenNoPlansExist() {
        // GIVEN
        when(planRepo.findAll()).thenReturn(Collections.emptyList());

        // WHEN
        List<WorkoutPlan> result =
                workoutPlanService.getAllWorkoutPlans();

        // THEN
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(planRepo).findAll();
    }

    @Test
    void getWorkoutPlanById_shouldReturnWorkoutPlan_whenPlanExists() {
        // GIVEN
        Long planId = 1L;
        when(planRepo.findById(planId))
                .thenReturn(Optional.of(workoutPlan));

        // WHEN
        WorkoutPlan result =
                workoutPlanService.getWorkoutPlanById(planId);

        // THEN
        assertEquals(workoutPlan, result);
        verify(planRepo).findById(planId);
    }

    @Test
    void getWorkoutPlanById_shouldThrowException_whenPlanNotFound() {
        // GIVEN
        Long planId = 1L;
        when(planRepo.findById(planId))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(
                PlanNotFoundException.class,
                () -> workoutPlanService.getWorkoutPlanById(planId)
        );

        verify(planRepo).findById(planId);
    }

    @Test
    void getWorkoutPlanDetailsById_shouldReturnWorkoutPlanDetails() {
        // GIVEN
        Long planId = 1L;

        Exercise exercise = Exercise.builder()
                .name("Bench Press")
                .build();

        WorkoutPlanItem item = WorkoutPlanItem.builder()
                .workoutPlan(workoutPlan)
                .exercise(exercise)
                .exercisePosition((short) 1)
                .targetWeightKg(new BigDecimal("60.00"))
                .targetRepMin((short) 8)
                .targetRepMax((short) 12)
                .comment("Pause at the bottom")
                .build();

        when(itemRepo
                .findAllByWorkoutPlan_PlanIdOrderByExercisePositionAsc(planId))
                .thenReturn(List.of(item));

        when(planRepo.findById(planId))
                .thenReturn(Optional.of(workoutPlan));

        // WHEN
        WorkoutPlanDetailsDTO result =
                workoutPlanService.getWorkoutPlanDetailsById(planId);

        // THEN
        assertEquals(workoutPlan.getDay(), result.day());
        assertEquals(workoutPlan.getName(), result.name());
        assertEquals(workoutPlan.getComment(), result.comment());
        assertEquals(1, result.items().size());

        WorkoutPlanItemDetailsDTO itemResult = result.items().get(0);

        assertEquals((short) 1, itemResult.exercisePosition());
        assertEquals("Bench Press", itemResult.exerciseName());
        assertEquals(new BigDecimal("60.00"), itemResult.targetWeightKg());
        assertEquals((short) 8, itemResult.targetRepMin());
        assertEquals((short) 12, itemResult.targetRepMax());
        assertEquals("Pause at the bottom", itemResult.comment());

        verify(itemRepo)
                .findAllByWorkoutPlan_PlanIdOrderByExercisePositionAsc(planId);
        verify(planRepo).findById(planId);
    }

    @Test
    void createWorkoutPlan_shouldSaveWorkoutPlan() {
        // GIVEN
        Day day = Day.DAY2;
        String name = "Pull Day";
        String comment = "Back and biceps";

        when(planRepo.existsByNameIgnoreCase(name))
                .thenReturn(false);

        when(planRepo.save(any(WorkoutPlan.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        WorkoutPlan result =
                workoutPlanService.createWorkoutPlan(
                        day,
                        name,
                        comment
                );

        // THEN
        assertNotNull(result);
        assertEquals(Day.DAY2, result.getDay());
        assertEquals(name, result.getName());
        assertEquals(comment, result.getComment());

        verify(planRepo).existsByNameIgnoreCase(name);
        verify(planRepo).save(any(WorkoutPlan.class));
    }

    @Test
    void createWorkoutPlan_shouldThrowException_whenPlanAlreadyExists() {
        // GIVEN
        String name = "Push Day";

        when(planRepo.existsByNameIgnoreCase(name))
                .thenReturn(true);

        // WHEN & THEN
        assertThrows(
                PlanAlreadyExistsException.class,
                () -> workoutPlanService.createWorkoutPlan(
                        Day.DAY1,
                        name,
                        "Chest, shoulders and triceps"
                )
        );

        verify(planRepo).existsByNameIgnoreCase(name);
        verify(planRepo, never())
                .save(any(WorkoutPlan.class));
    }

    @Test
    void updateWorkoutPlan_shouldUpdateAndReturnPlan_whenPlanExists() {
        // GIVEN
        Long planId = 1L;

        WorkoutPlanRequest updatedPlan = new WorkoutPlanRequest(
                Day.DAY3,
                "Leg Day",
                "Quads, hamstrings and calves"
        );

        when(planRepo.findById(planId))
                .thenReturn(Optional.of(workoutPlan));

        // WHEN
        WorkoutPlan result = workoutPlanService.updateWorkoutPlan(
                planId,
                updatedPlan
        );

        // THEN
        assertSame(workoutPlan, result);
        assertEquals(Day.DAY3, result.getDay());
        assertEquals("Leg Day", result.getName());
        assertEquals(
                "Quads, hamstrings and calves",
                result.getComment()
        );

        verify(planRepo).findById(planId);
    }

    @Test
    void updateWorkoutPlan_shouldThrowException_whenPlanNotFound() {
        // GIVEN
        Long planId = 1L;

        WorkoutPlanRequest updatedPlan = new WorkoutPlanRequest(
                Day.DAY3,
                "Leg Day",
                "Quads, hamstrings and calves"
        );

        when(planRepo.findById(planId))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(
                PlanNotFoundException.class,
                () -> workoutPlanService.updateWorkoutPlan(
                        planId,
                        updatedPlan
                )
        );

        verify(planRepo).findById(planId);
    }

    @Test
    void deleteWorkoutPlan_shouldDeleteWorkoutPlan_whenPlanExists() {
        // GIVEN
        Long planId = 1L;

        when(planRepo.findById(planId))
                .thenReturn(Optional.of(workoutPlan));

        // WHEN
        workoutPlanService.deleteWorkoutPlan(planId);

        // THEN
        verify(planRepo).findById(planId);
        verify(planRepo).delete(workoutPlan);
    }

    @Test
    void deleteWorkoutPlan_shouldThrowException_whenPlanNotFound() {
        // GIVEN
        Long planId = 1L;

        when(planRepo.findById(planId))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(
                PlanNotFoundException.class,
                () -> workoutPlanService.deleteWorkoutPlan(planId)
        );

        verify(planRepo).findById(planId);
        verify(planRepo, never())
                .delete(any(WorkoutPlan.class));
    }
}