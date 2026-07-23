package com.tjost.workouttracker.service;

import com.tjost.workouttracker.exception.ExerciseAlreadyExistsException;
import com.tjost.workouttracker.exception.ExerciseNotFoundException;
import com.tjost.workouttracker.model.Exercise;
import com.tjost.workouttracker.repository.ExerciseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.never;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceTest {

    @Mock private ExerciseRepository exerciseRepo;

    @InjectMocks private ExerciseService exerciseService;

    private Exercise exercise;

    @BeforeEach
    void setUp() {
        exercise = new Exercise("Hack Squat Machine");
    }

    @Test
    void getAllExercises_shouldReturnListOfExercises() {
        // GIVEN
        List<Exercise> mockExercises = List.of(new Exercise("Bench Press"));
        when(exerciseRepo.findAll()).thenReturn(mockExercises);

        // WHEN
        List<Exercise> result = exerciseService.getAllExercises();

        // THEN
        assertEquals(mockExercises, result);
        verify(exerciseRepo).findAll();
    }



    @Test
    void getExerciseById_shouldReturnExercise_whenExists() {
        // GIVEN
        Exercise exercise = new Exercise("Bench Press");
        Long id = exercise.getExerciseId();
        when(exerciseRepo.findById(id)).thenReturn(Optional.of(exercise));

        // WHEN
        Exercise result = exerciseService.getExerciseById(id);

        // THEN
        assertEquals(exercise, result);
        verify(exerciseRepo).findById(id);
    }

    @Test
    void getExerciseById_shouldThrowException_whenNotFound() {
        // GIVEN
        when(exerciseRepo.findById(0L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ExerciseNotFoundException.class, () -> exerciseService.getExerciseById(0L));
        verify(exerciseRepo).findById(0L);
    }

    @Test
    void createExercise_shouldSaveExercise() {
        // GIVEN
        when(exerciseRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // WHEN
        Exercise result = exerciseService.createExercise("Curls");

        // THEN
        assertEquals("Curls", result.getName());
        assertNull(result.getExerciseId());
        verify(exerciseRepo).save(any(Exercise.class));
    }

    @Test
    void createExercise_shouldThrowException_whenExerciseAlreadyExists() {
        when(exerciseRepo.existsByNameIgnoreCase("Curls")).thenReturn(true);

        assertThrows(
                ExerciseAlreadyExistsException.class,
                () -> exerciseService.createExercise("Curls")
        );

        verify(exerciseRepo, never()).save(any());
    }

    @Test
    void updateExercise_shouldUpdateName_whenExerciseExists() {
        // GIVEN
        Long id = 1L;
        when(exerciseRepo.findById(id)).thenReturn(Optional.of(exercise));

        // WHEN
        Exercise result = exerciseService.updateExercise(id, "Leg Press");

        // THEN
        assertEquals("Leg Press", result.getName());
        verify(exerciseRepo).findById(id);
    }

    @Test
    void updateExercise_shouldThrowException_whenExerciseNotFound() {
        // GIVEN
        Long id = 1L;
        when(exerciseRepo.findById(id)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(
                ExerciseNotFoundException.class,
                () -> exerciseService.updateExercise(id, "Leg Press")
        );

        verify(exerciseRepo).findById(id);
    }

    @Test
    void deleteExercise_shouldDeleteExercise_whenExerciseExists() {
        // GIVEN
        Long id = 1L;
        when(exerciseRepo.findById(id)).thenReturn(Optional.of(exercise));

        // WHEN
        exerciseService.deleteExercise(id);

        // THEN
        verify(exerciseRepo).findById(id);
        verify(exerciseRepo).delete(exercise);
    }

    @Test
    void deleteExercise_shouldThrowException_whenExerciseNotFound() {
        // GIVEN
        Long id = 1L;
        when(exerciseRepo.findById(id)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(
                ExerciseNotFoundException.class,
                () -> exerciseService.deleteExercise(id)
        );

        verify(exerciseRepo).findById(id);
        verify(exerciseRepo, never()).delete(any(Exercise.class));
    }

}
