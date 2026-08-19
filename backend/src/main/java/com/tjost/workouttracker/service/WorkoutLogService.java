package com.tjost.workouttracker.service;

import com.tjost.workouttracker.dto.WorkoutLogRequest;
import com.tjost.workouttracker.exception.LogNotFoundException;
import com.tjost.workouttracker.exception.LogNotFoundExceptionWorkoutDate;
import com.tjost.workouttracker.exception.PlanNotFoundException;
import com.tjost.workouttracker.model.WorkoutLog;
import com.tjost.workouttracker.model.WorkoutPlan;
import com.tjost.workouttracker.repository.WorkoutLogRepository;
import com.tjost.workouttracker.repository.WorkoutPlanRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class WorkoutLogService {

    private final WorkoutPlanRepository planRepo;
    private final WorkoutLogRepository logRepo;

    public WorkoutLog createWorkoutLog (
            Long planId,
            LocalDate workoutDate,
            String comment,
            OffsetDateTime createdAt
    ) {
        WorkoutPlan plan = planRepo.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(planId));

        WorkoutLog log = WorkoutLog.builder()
                .workoutPlan(plan)
                .workoutDate(workoutDate)
                .comment(comment)
                .createdAt(createdAt)
                .build();

        return logRepo.save(log);
    }

    public WorkoutLog getWorkoutLogById(Long logId) {
        return logRepo.findById(logId)
                .orElseThrow(() -> new LogNotFoundException(logId));
    }

    public WorkoutLog getWorkoutLogByWorkoutDate(LocalDate workoutDate) {
        return logRepo.findByWorkoutDate(workoutDate)
                .orElseThrow(() -> new LogNotFoundExceptionWorkoutDate(workoutDate));
    }

    @Transactional
    public WorkoutLog updateWorkoutLog(Long logId, WorkoutLogRequest request) {
        WorkoutLog existingLog = logRepo.findById(logId)
                .orElseThrow(() -> new LogNotFoundException(logId));

        existingLog.setWorkoutDate(request.workoutDate());
        existingLog.setComment(request.comment());
        existingLog.setCreatedAt(request.createdAt());

        return existingLog;
    }

    @Transactional
    public void deleteWorkoutLog(Long logId){
        WorkoutLog log = logRepo.findById(logId)
                .orElseThrow(() -> new LogNotFoundException(logId));
        logRepo.delete(log);
    }

}
