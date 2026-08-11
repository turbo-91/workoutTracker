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
class WorkoutPlanControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WorkoutPlanRepository planRepo;

    @Autowired
    private WorkoutPlanItemRepository itemRepo;

    @Autowired
    private ExerciseRepository exerciseRepo;


    @DirtiesContext
    @Test
    void getAllWorkoutPlans_shouldReturnEmptyList_whenRepositoryIsEmpty()
            throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/api/workout-plans"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }


    @DirtiesContext
    @Test
    void getAllWorkoutPlans_shouldReturnListWithOnePlan_whenOnePlanWasSaved()
            throws Exception {

        // Arrange
        WorkoutPlan plan = createAndSavePlan();

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get("/api/workout-plans"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].planId")
                        .value(plan.getPlanId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].day")
                        .value("DAY1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name")
                        .value("Push Day"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].comment")
                        .value("Test plan"));
    }


    @DirtiesContext
    @Test
    void getWorkoutPlanById_shouldReturnPlan_whenPlanExists()
            throws Exception {

        // Arrange
        WorkoutPlan plan = createAndSavePlan();

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get(
                        "/api/workout-plans/{id}",
                        plan.getPlanId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.planId")
                        .value(plan.getPlanId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.day")
                        .value("DAY1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                        .value("Push Day"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment")
                        .value("Test plan"));
    }


    @DirtiesContext
    @Test
    void getWorkoutPlanById_shouldReturnNotFound_whenPlanDoesNotExist()
            throws Exception {

        mvc.perform(MockMvcRequestBuilders.get(
                        "/api/workout-plans/{id}",
                        999L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @DirtiesContext
    @Test
    void createWorkoutPlan_shouldReturnCreatedPlan_whenValidRequestIsSent()
            throws Exception {

        // Arrange
        String planJson = """
                {
                    "day": "DAY1",
                    "name": "Push Day",
                    "comment": "Chest and shoulders"
                }
                """;

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.post("/api/workout-plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(planJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.planId")
                        .isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.day")
                        .value("DAY1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                        .value("Push Day"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment")
                        .value("Chest and shoulders"));

        // Verify
        assertTrue(planRepo.existsByNameIgnoreCase("Push Day"));
    }


    @DirtiesContext
    @Test
    void createWorkoutPlan_shouldReturnConflict_whenPlanAlreadyExists()
            throws Exception {

        // Arrange
        createAndSavePlan();

        String planJson = """
                {
                    "day": "DAY2",
                    "name": "Push Day",
                    "comment": "Another plan"
                }
                """;

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.post("/api/workout-plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(planJson))
                .andExpect(MockMvcResultMatchers.status().isConflict());

        // Verify
        assertEquals(1, planRepo.count());
    }


    @DirtiesContext
    @Test
    void createWorkoutPlan_shouldReturnConflict_whenPlanExistsWithDifferentCase()
            throws Exception {

        // Arrange
        createAndSavePlan();

        String planJson = """
                {
                    "day": "DAY2",
                    "name": "push day",
                    "comment": "Another plan"
                }
                """;

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.post("/api/workout-plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(planJson))
                .andExpect(MockMvcResultMatchers.status().isConflict());

        // Verify
        assertEquals(1, planRepo.count());
    }


    @DirtiesContext
    @Test
    void updateWorkoutPlan_shouldReturnUpdatedPlan_whenPlanExists()
            throws Exception {

        // Arrange
        WorkoutPlan plan = createAndSavePlan();

        String updatedPlanJson = """
                {
                    "day": "DAY2",
                    "name": "Pull Day",
                    "comment": "Back and biceps"
                }
                """;

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.put(
                                "/api/workout-plans/{id}",
                                plan.getPlanId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedPlanJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.planId")
                        .value(plan.getPlanId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.day")
                        .value("DAY2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                        .value("Pull Day"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment")
                        .value("Back and biceps"));

        // Verify
        WorkoutPlan updatedPlan = planRepo.findById(plan.getPlanId())
                .orElseThrow();

        assertEquals(Day.DAY2, updatedPlan.getDay());
        assertEquals("Pull Day", updatedPlan.getName());
        assertEquals("Back and biceps", updatedPlan.getComment());
    }


    @DirtiesContext
    @Test
    void updateWorkoutPlan_shouldReturnNotFound_whenPlanDoesNotExist()
            throws Exception {

        // Arrange
        String updatedPlanJson = """
                {
                    "day": "DAY2",
                    "name": "Pull Day",
                    "comment": "Back and biceps"
                }
                """;

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.put(
                                "/api/workout-plans/{id}",
                                999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedPlanJson))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @DirtiesContext
    @Test
    void deleteWorkoutPlan_shouldReturnNoContent_whenPlanExists()
            throws Exception {

        // Arrange
        WorkoutPlan plan = createAndSavePlan();

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.delete(
                        "/api/workout-plans/{id}",
                        plan.getPlanId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Verify
        assertFalse(planRepo.existsById(plan.getPlanId()));
    }


    @DirtiesContext
    @Test
    void deleteWorkoutPlan_shouldReturnNotFound_whenPlanDoesNotExist()
            throws Exception {

        mvc.perform(MockMvcRequestBuilders.delete(
                        "/api/workout-plans/{id}",
                        999L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @DirtiesContext
    @Test
    void getWorkoutPlanDetailsById_shouldReturnPlanDetails_whenPlanExists()
            throws Exception {

        // Arrange
        WorkoutPlan plan = createAndSavePlan();

        Exercise benchPress = createAndSaveExercise("Bench Press");
        Exercise shoulderPress = createAndSaveExercise("Shoulder Press");

        createAndSaveItem(
                plan,
                benchPress,
                (short) 1,
                new BigDecimal("80.0"),
                (short) 8,
                (short) 12,
                "Focus on form"
        );

        createAndSaveItem(
                plan,
                shoulderPress,
                (short) 2,
                new BigDecimal("40.0"),
                (short) 10,
                (short) 12,
                "Controlled reps"
        );

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get(
                        "/api/workout-plans/{id}/details",
                        plan.getPlanId()))
                .andExpect(MockMvcResultMatchers.status().isOk())

                .andExpect(MockMvcResultMatchers.jsonPath("$.day")
                        .value("DAY1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                        .value("Push Day"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment")
                        .value("Test plan"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.items.length()")
                        .value(2))

                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].exercisePosition")
                        .value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].exerciseName")
                        .value("Bench Press"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].targetWeightKg")
                        .value(80.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].targetRepMin")
                        .value(8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].targetRepMax")
                        .value(12))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].comment")
                        .value("Focus on form"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].exercisePosition")
                        .value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].exerciseName")
                        .value("Shoulder Press"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].targetWeightKg")
                        .value(40.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].targetRepMin")
                        .value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].targetRepMax")
                        .value(12))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].comment")
                        .value("Controlled reps"));
    }


    @DirtiesContext
    @Test
    void getWorkoutPlanDetailsById_shouldReturnDetailsWithEmptyItems_whenPlanHasNoItems()
            throws Exception {

        // Arrange
        WorkoutPlan plan = createAndSavePlan();

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get(
                        "/api/workout-plans/{id}/details",
                        plan.getPlanId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.day")
                        .value("DAY1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                        .value("Push Day"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items")
                        .isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment")
                        .value("Test plan"));
    }


    @DirtiesContext
    @Test
    void getWorkoutPlanDetailsById_shouldReturnNotFound_whenPlanDoesNotExist()
            throws Exception {

        mvc.perform(MockMvcRequestBuilders.get(
                        "/api/workout-plans/{id}/details",
                        999L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    private WorkoutPlan createAndSavePlan() {
        WorkoutPlan plan = WorkoutPlan.builder()
                .day(Day.DAY1)
                .name("Push Day")
                .comment("Test plan")
                .build();

        return planRepo.save(plan);
    }


    private Exercise createAndSaveExercise(String name) {
        Exercise exercise = new Exercise(name);

        return exerciseRepo.save(exercise);
    }


    private WorkoutPlanItem createAndSaveItem(
            WorkoutPlan plan,
            Exercise exercise,
            short exercisePosition,
            BigDecimal targetWeightKg,
            short targetRepMin,
            short targetRepMax,
            String comment
    ) {
        WorkoutPlanItem item = WorkoutPlanItem.builder()
                .workoutPlan(plan)
                .exercise(exercise)
                .exercisePosition(exercisePosition)
                .targetWeightKg(targetWeightKg)
                .targetRepMin(targetRepMin)
                .targetRepMax(targetRepMax)
                .comment(comment)
                .build();

        return itemRepo.save(item);
    }
}