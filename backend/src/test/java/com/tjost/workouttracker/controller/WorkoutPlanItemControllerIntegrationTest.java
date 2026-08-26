package com.tjost.workouttracker.controller;

import com.tjost.workouttracker.model.Exercise;
import com.tjost.workouttracker.model.WorkoutPlan;
import com.tjost.workouttracker.model.WorkoutPlanItem;
import com.tjost.workouttracker.model.enums.Day;
import com.tjost.workouttracker.repository.ExerciseRepository;
import com.tjost.workouttracker.repository.WorkoutPlanItemRepository;
import com.tjost.workouttracker.repository.WorkoutPlanRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class WorkoutPlanItemControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WorkoutPlanItemRepository itemRepo;

    @Autowired
    private WorkoutPlanRepository planRepo;

    @Autowired
    private ExerciseRepository exerciseRepo;


    @DirtiesContext
    @Test
    void createWorkoutPlanItem_shouldReturnCreatedItem_whenValidRequestIsSent()
            throws Exception {

        // Arrange
        WorkoutPlan plan = createAndSavePlan();
        Exercise exercise = createAndSaveExercise();

        String itemJson = """
                {
                    "planId": %d,
                    "exerciseId": %d,
                    "exercisePosition": 1,
                    "targetWeightKg": 80.0,
                    "targetRepMin": 8,
                    "targetRepMax": 12,
                    "comment": "Focus on form"
                }
                """.formatted(
                plan.getPlanId(),
                exercise.getExerciseId()
        );

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.post("/api/workout-plan-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.planItemId").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.planId")
                        .value(plan.getPlanId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.exerciseId")
                        .value(exercise.getExerciseId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.exercisePosition")
                        .value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.targetWeightKg")
                        .value(80.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.targetRepMin")
                        .value(8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.targetRepMax")
                        .value(12))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment")
                        .value("Focus on form"));

        // Verify
        assertEquals(1, itemRepo.count());

        WorkoutPlanItem savedItem = itemRepo.findAll().get(0);

        assertEquals(plan.getPlanId(), savedItem.getWorkoutPlan().getPlanId());
        assertEquals(exercise.getExerciseId(), savedItem.getExercise().getExerciseId());
        assertEquals((short) 1, savedItem.getExercisePosition());
        assertEquals(0, new BigDecimal("80.0")
                .compareTo(savedItem.getTargetWeightKg()));
        assertEquals((short) 8, savedItem.getTargetRepMin());
        assertEquals((short) 12, savedItem.getTargetRepMax());
        assertEquals("Focus on form", savedItem.getComment());
    }


    @DirtiesContext
    @Test
    void createWorkoutPlanItem_shouldReturnNotFound_whenPlanDoesNotExist()
            throws Exception {

        // Arrange
        Exercise exercise = createAndSaveExercise();

        String itemJson = """
                {
                    "planId": 999,
                    "exerciseId": %d,
                    "exercisePosition": 1,
                    "targetWeightKg": 80.0,
                    "targetRepMin": 8,
                    "targetRepMax": 12,
                    "comment": "Focus on form"
                }
                """.formatted(exercise.getExerciseId());

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.post("/api/workout-plan-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // Verify
        assertEquals(0, itemRepo.count());
    }


    @DirtiesContext
    @Test
    void createWorkoutPlanItem_shouldReturnNotFound_whenExerciseDoesNotExist()
            throws Exception {

        // Arrange
        WorkoutPlan plan = createAndSavePlan();

        String itemJson = """
                {
                    "planId": %d,
                    "exerciseId": 999,
                    "exercisePosition": 1,
                    "targetWeightKg": 80.0,
                    "targetRepMin": 8,
                    "targetRepMax": 12,
                    "comment": "Focus on form"
                }
                """.formatted(plan.getPlanId());

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.post("/api/workout-plan-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // Verify
        assertEquals(0, itemRepo.count());
    }


    @DirtiesContext
    @Test
    void getWorkoutPlanItemById_shouldReturnItem_whenItemExists()
            throws Exception {

        // Arrange
        WorkoutPlan plan = createAndSavePlan();
        Exercise exercise = createAndSaveExercise();

        WorkoutPlanItem item = createAndSaveItem(plan, exercise);

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get(
                        "/api/workout-plan-items/{id}",
                        item.getPlanItemId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.planItemId")
                        .value(item.getPlanItemId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.planId")
                        .value(plan.getPlanId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.exerciseId")
                        .value(exercise.getExerciseId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.exercisePosition")
                        .value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.targetWeightKg")
                        .value(80.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.targetRepMin")
                        .value(8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.targetRepMax")
                        .value(12))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment")
                        .value("Focus on form"));
    }


    @DirtiesContext
    @Test
    void getWorkoutPlanItemById_shouldReturnNotFound_whenItemDoesNotExist()
            throws Exception {

        mvc.perform(MockMvcRequestBuilders.get(
                        "/api/workout-plan-items/{id}",
                        999L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @DirtiesContext
    @Test
    void updateWorkoutPlanItem_shouldReturnUpdatedItem_whenItemExists()
            throws Exception {

        // Arrange
        WorkoutPlan plan = createAndSavePlan();
        Exercise exercise = createAndSaveExercise();

        WorkoutPlanItem existingItem = createAndSaveItem(plan, exercise);

        String updatedItemJson = """
                {
                    "planId": %d,
                    "exerciseId": %d,
                    "exercisePosition": 2,
                    "targetWeightKg": 90.0,
                    "targetRepMin": 6,
                    "targetRepMax": 10,
                    "comment": "Increase weight"
                }
                """.formatted(
                plan.getPlanId(),
                exercise.getExerciseId()
        );

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.put(
                                "/api/workout-plan-items/{id}",
                                existingItem.getPlanItemId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedItemJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.planItemId")
                        .value(existingItem.getPlanItemId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.exercisePosition")
                        .value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.targetWeightKg")
                        .value(90.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.targetRepMin")
                        .value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.targetRepMax")
                        .value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment")
                        .value("Increase weight"));

        // Verify
        WorkoutPlanItem updatedItem =
                itemRepo.findById(existingItem.getPlanItemId()).orElseThrow();

        assertEquals((short) 2, updatedItem.getExercisePosition());
        assertEquals(0, new BigDecimal("90.0")
                .compareTo(updatedItem.getTargetWeightKg()));
        assertEquals((short) 6, updatedItem.getTargetRepMin());
        assertEquals((short) 10, updatedItem.getTargetRepMax());
        assertEquals("Increase weight", updatedItem.getComment());
    }


    @DirtiesContext
    @Test
    void updateWorkoutPlanItem_shouldReturnNotFound_whenItemDoesNotExist()
            throws Exception {

        // Arrange
        WorkoutPlan plan = createAndSavePlan();
        Exercise exercise = createAndSaveExercise();

        String updatedItemJson = """
                {
                    "planId": %d,
                    "exerciseId": %d,
                    "exercisePosition": 2,
                    "targetWeightKg": 90.0,
                    "targetRepMin": 6,
                    "targetRepMax": 10,
                    "comment": "Increase weight"
                }
                """.formatted(
                plan.getPlanId(),
                exercise.getExerciseId()
        );

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.put(
                                "/api/workout-plan-items/{id}",
                                999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedItemJson))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @DirtiesContext
    @Test
    void deleteWorkoutPlanItem_shouldReturnNoContent_whenItemExists()
            throws Exception {

        // Arrange
        WorkoutPlan plan = createAndSavePlan();
        Exercise exercise = createAndSaveExercise();

        WorkoutPlanItem existingItem = createAndSaveItem(plan, exercise);

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.delete(
                        "/api/workout-plan-items/{id}",
                        existingItem.getPlanItemId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Verify
        assertFalse(itemRepo.existsById(existingItem.getPlanItemId()));
    }


    @DirtiesContext
    @Test
    void deleteWorkoutPlanItem_shouldReturnNotFound_whenItemDoesNotExist()
            throws Exception {

        mvc.perform(MockMvcRequestBuilders.delete(
                        "/api/workout-plan-items/{id}",
                        999L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    private WorkoutPlan createAndSavePlan() {
        WorkoutPlan plan = WorkoutPlan.builder()
                .name("Push Day")
                .day(Day.DAY1)
                .comment("Test plan")
                .build();

        return planRepo.save(plan);
    }


    private Exercise createAndSaveExercise() {
        Exercise exercise = new Exercise("Bench Press");

        return exerciseRepo.save(exercise);
    }


    private WorkoutPlanItem createAndSaveItem(
            WorkoutPlan plan,
            Exercise exercise
    ) {
        WorkoutPlanItem item = WorkoutPlanItem.builder()
                .workoutPlan(plan)
                .exercise(exercise)
                .exercisePosition((short) 1)
                .targetWeightKg(new BigDecimal("80.0"))
                .targetRepMin((short) 8)
                .targetRepMax((short) 12)
                .comment("Focus on form")
                .build();

        return itemRepo.save(item);
    }
}