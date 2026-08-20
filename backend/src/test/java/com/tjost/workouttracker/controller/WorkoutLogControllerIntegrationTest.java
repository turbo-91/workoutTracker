package com.tjost.workouttracker.controller;

import com.tjost.workouttracker.model.WorkoutLog;
import com.tjost.workouttracker.model.WorkoutPlan;
import com.tjost.workouttracker.model.enums.Day;
import com.tjost.workouttracker.repository.WorkoutLogRepository;
import com.tjost.workouttracker.repository.WorkoutPlanRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class WorkoutLogControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WorkoutLogRepository logRepo;

    @Autowired
    private WorkoutPlanRepository planRepo;

    private WorkoutPlan saveWorkoutPlan(
            Day day,
            String name,
            String comment
    ) {
        return planRepo.save(
                WorkoutPlan.builder()
                        .day(day)
                        .name(name)
                        .comment(comment)
                        .build()
        );
    }

    private WorkoutLog saveLog(
            WorkoutPlan workoutPlan,
            LocalDate workoutDate,
            String comment,
            OffsetDateTime createdAt
    ) {
        return logRepo.save(
                WorkoutLog.builder()
                        .workoutPlan(workoutPlan)
                        .workoutDate(workoutDate)
                        .comment(comment)
                        .createdAt(createdAt)
                        .build()
        );
    }

    @DirtiesContext
    @Test
    void createWorkoutLog_shouldReturnCreatedLogResponse_whenValidRequestIsSent() throws Exception {
        // Arrange
        WorkoutPlan savedPlan = saveWorkoutPlan(
                Day.DAY1,
                "Push Day",
                "Chest and triceps"
        );

        String logJson = """
        {
          "planId": %d,
          "workoutDate": "2026-08-20",
          "comment": "Good workout",
          "createdAt": "2026-08-20T10:00:00+02:00"
        }
        """.formatted(savedPlan.getPlanId());;
        OffsetDateTime createdAt = OffsetDateTime.parse("2026-08-20T10:00:00+02:00");

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.post("/api/workout-logs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(logJson))

                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.planId").value(savedPlan.getPlanId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.workoutDate").value("2026-08-20"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").value("Good workout"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt")
                        .value("2026-08-20T08:00:00Z"));

        // Verify
        assertTrue(logRepo.existsByCreatedAt(createdAt));

    }

    @DirtiesContext
    @Test
    void createLog_shouldReturnConflict_whenLogAlreadyExists() throws Exception {
        // Arrange
        WorkoutPlan savedPlan = saveWorkoutPlan(
                Day.DAY1,
                "Push Day",
                "Chest and triceps"
        );

        saveLog(
                savedPlan,
                LocalDate.of(2026, 8, 20),
                "Existing workout",
                OffsetDateTime.parse("2026-08-20T08:00:00Z")
        );

        String logJson = """
        {
          "planId": %d,
          "workoutDate": "2026-08-20",
          "comment": "Good workout",
          "createdAt": "2026-08-20T10:00:00+02:00"
        }
        """.formatted(savedPlan.getPlanId());;

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.post("/api/workout-logs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(logJson))
                .andExpect(MockMvcResultMatchers.status().isConflict());

    }

    @DirtiesContext
    @Test
    void getWorkoutLogById_shouldReturnLog_whenLogExists() throws Exception {
        // Arrange
        WorkoutPlan savedPlan = saveWorkoutPlan(
                Day.DAY1,
                "Push Day",
                "Chest and triceps"
        );

        WorkoutLog savedLog = saveLog(
                savedPlan,
                LocalDate.of(2026, 8, 20),
                "Good workout",
                OffsetDateTime.parse("2026-08-20T10:00:00+02:00")
        );

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get(
                        "/api/workout-logs/{id}", savedLog.getLogId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
        {
          "logId": %d,
          "planId": %d,
          "workoutDate": "2026-08-20",
          "comment": "Good workout",
          "createdAt": "2026-08-20T10:00:00+02:00"
        }
        """.formatted(
                        savedLog.getLogId(),
                        savedPlan.getPlanId()
                )));

    }

    @DirtiesContext
    @Test
    void getWorkoutLogById_shouldReturnNotFound_whenLogDoesNotExist() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/workout-logs/{id}", 999L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @DirtiesContext
    @Test
    void updateLog_shouldReturnUpdatedLogResponse_whenLogExists() throws Exception {
        // Arrange
        WorkoutPlan savedPlan = saveWorkoutPlan(
                Day.DAY1,
                "Push Day",
                "Chest and triceps"
        );

        WorkoutLog savedLog = saveLog(
                savedPlan,
                LocalDate.of(2026, 8, 20),
                "Existing workout",
                OffsetDateTime.parse("2026-08-20T08:00:00Z")
        );

        String updatedLogJson = """
        {
          "planId": %d,
          "workoutDate": "2026-08-20",
          "comment": "Updated workout",
          "createdAt": "2026-08-20T10:00:00+02:00"
        }
        """.formatted(savedPlan.getPlanId());

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.put("/api/workout-logs/{id}", savedLog.getLogId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedLogJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                            "logId": %d,
                            "planId": %d,
                            "workoutDate": "2026-08-20",
                            "comment": "Updated workout",
                            "createdAt": "2026-08-20T08:00:00Z"
                        }
                        """.formatted(
                        savedLog.getLogId(),
                        savedPlan.getPlanId())));
        // Verify
        WorkoutLog updatedLog = logRepo.findById(savedLog.getLogId())
                .orElseThrow();

        assertEquals("Updated workout", updatedLog.getComment());
    }

    @DirtiesContext
    @Test
    void updateLog_shouldReturnNotFound_whenLogDoesNotExist() throws Exception {
        // Arrange
        WorkoutPlan savedPlan = saveWorkoutPlan(
                Day.DAY1,
                "Push Day",
                "Chest and triceps"
        );

        String updatedLogJson = """
            {
              "planId": %d,
              "workoutDate": "2026-08-20",
              "comment": "Updated workout",
              "createdAt": "2026-08-20T10:00:00+02:00"
            }
            """.formatted(savedPlan.getPlanId());

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.put("/api/workout-logs/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedLogJson))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @DirtiesContext
    @Test
    void updateLog_shouldReturnNotFound_whenPlanDoesNotExist() throws Exception {
        // Arrange
        WorkoutPlan savedPlan = saveWorkoutPlan(
                Day.DAY1,
                "Push Day",
                "Chest and triceps"
        );

        WorkoutLog savedLog = saveLog(
                savedPlan,
                LocalDate.of(2026, 8, 20),
                "Existing workout",
                OffsetDateTime.parse("2026-08-20T08:00:00Z")
        );

        String updatedLogJson = """
            {
              "planId": 999,
              "workoutDate": "2026-08-20",
              "comment": "Updated workout",
              "createdAt": "2026-08-20T10:00:00+02:00"
            }
            """;

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.put(
                                "/api/workout-logs/{id}",
                                savedLog.getLogId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedLogJson))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @DirtiesContext
    @Test
    void deleteLog_shouldReturnNoContent_whenLogExists() throws Exception {
        // Arrange
        WorkoutPlan savedPlan = saveWorkoutPlan(
                Day.DAY1,
                "Push Day",
                "Chest and triceps"
        );

        WorkoutLog savedLog = saveLog(
                savedPlan,
                LocalDate.of(2026, 8, 20),
                "Existing workout",
                OffsetDateTime.parse("2026-08-20T08:00:00Z")
        );

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.delete(
                        "/api/workout-logs/{id}",
                        savedLog.getLogId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Verify
        assertFalse(logRepo.existsById(savedLog.getLogId()));
    }

    @DirtiesContext
    @Test
    void deleteLog_shouldReturnNotFound_whenLogDoesNotExist() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(
                        "/api/workout-logs/{id}",
                        999L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}

