package com.tjost.workouttracker.controller;

import com.tjost.workouttracker.model.Exercise;
import com.tjost.workouttracker.repository.ExerciseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.test.context.ActiveProfiles;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class ExerciseControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ExerciseRepository exerciseRepo;


    @DirtiesContext
    @Test
    void getAllExercises_shouldReturnEmptyList_whenRepositoryIsEmpty() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/exercises"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }


    @DirtiesContext
    @Test
    void getAllExercises_shouldReturnListWithOneExercise_whenOneExerciseWasSavedInRepository()
            throws Exception {

        // Arrange
        Exercise exercise = new Exercise("Bench Press");
        exerciseRepo.save(exercise);

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get("/api/exercises"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        [
                            {
                                "exerciseId": %d,
                                "name": "Bench Press"
                            }
                        ]
                        """.formatted(exercise.getExerciseId())));
    }


    @DirtiesContext
    @Test
    void getExerciseById_shouldReturnExercise_whenExerciseExists() throws Exception {
        // Arrange
        Exercise exercise = new Exercise("Bench Press");
        Exercise savedExercise = exerciseRepo.save(exercise);

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get(
                        "/api/exercises/{id}",
                        savedExercise.getExerciseId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                            "exerciseId": %d,
                            "name": "Bench Press"
                        }
                        """.formatted(savedExercise.getExerciseId())));
    }


    @DirtiesContext
    @Test
    void getExerciseById_shouldReturnNotFound_whenExerciseDoesNotExist() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/exercises/{id}", 999L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @DirtiesContext
    @Test
    void createExercise_shouldReturnCreatedExercise_whenValidRequestIsSent() throws Exception {
        // Arrange
        String exerciseJson = """
                {
                    "name": "Squat"
                }
                """;

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.post("/api/exercises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(exerciseJson))

                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                        .value("Squat"));

        // Verify
        assertTrue(exerciseRepo.existsByNameIgnoreCase("Squat"));
    }


    @DirtiesContext
    @Test
    void createExercise_shouldReturnConflict_whenExerciseAlreadyExists() throws Exception {
        // Arrange
        Exercise existingExercise = new Exercise("Squat");
        exerciseRepo.save(existingExercise);

        String exerciseJson = """
                {
                    "name": "Squat"
                }
                """;

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.post("/api/exercises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(exerciseJson))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }


    @DirtiesContext
    @Test
    void createExercise_shouldReturnConflict_whenExerciseExistsWithDifferentCase()
            throws Exception {

        // Arrange
        Exercise existingExercise = new Exercise("Squat");
        exerciseRepo.save(existingExercise);

        String exerciseJson = """
                {
                    "name": "squat"
                }
                """;

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.post("/api/exercises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(exerciseJson))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }


    @DirtiesContext
    @Test
    void updateExercise_shouldReturnUpdatedExercise_whenExerciseExists() throws Exception {
        // Arrange
        Exercise existingExercise = new Exercise("Bench Press");
        Exercise savedExercise = exerciseRepo.save(existingExercise);

        String updatedExerciseJson = """
                {
                    "name": "Incline Bench Press"
                }
                """;

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.put(
                                "/api/exercises/{id}",
                                savedExercise.getExerciseId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedExerciseJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                            "exerciseId": %d,
                            "name": "Incline Bench Press"
                        }
                        """.formatted(savedExercise.getExerciseId())));

        // Verify
        Exercise updatedExercise = exerciseRepo.findById(savedExercise.getExerciseId())
                .orElseThrow();

        assertEquals("Incline Bench Press", updatedExercise.getName());
    }


    @DirtiesContext
    @Test
    void updateExercise_shouldReturnNotFound_whenExerciseDoesNotExist() throws Exception {
        // Arrange
        String updatedExerciseJson = """
                {
                    "name": "Incline Bench Press"
                }
                """;

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.put("/api/exercises/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedExerciseJson))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @DirtiesContext
    @Test
    void deleteExercise_shouldReturnNoContent_whenExerciseExists() throws Exception {
        // Arrange
        Exercise existingExercise = new Exercise("Deadlift");
        Exercise savedExercise = exerciseRepo.save(existingExercise);

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.delete(
                        "/api/exercises/{id}",
                        savedExercise.getExerciseId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Verify
        assertFalse(exerciseRepo.existsById(savedExercise.getExerciseId()));
    }


    @DirtiesContext
    @Test
    void deleteExercise_shouldReturnNotFound_whenExerciseDoesNotExist() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/api/exercises/{id}", 999L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
