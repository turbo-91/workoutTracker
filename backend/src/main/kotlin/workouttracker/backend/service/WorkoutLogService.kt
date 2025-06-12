package workouttracker.backend.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import workouttracker.backend.exception.WorkoutLogNotFoundException
import workouttracker.backend.model.WorkoutLog
import workouttracker.backend.model.enums.Mon
import workouttracker.backend.model.enums.TrainingGoal
import workouttracker.backend.repository.WorkoutLogRepo
import java.time.LocalDateTime
import java.util.*

class WorkoutLogService(private val workoutLogRepo: WorkoutLogRepo) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(WorkoutService::class.java)
    }

    fun getWorkoutLogById(id: String): Optional<WorkoutLog> {
        return workoutLogRepo.findById(id);
    }

    fun getAllWorkoutLogs(): List<WorkoutLog> {
        return workoutLogRepo.findAll();
    }

    fun getWorkoutLogByCreatedAt(createdAt: LocalDateTime): List<WorkoutLog> {
        if (!workoutLogRepo.existsByCreatedAt(createdAt)) {
            throw WorkoutLogNotFoundException("No workout log with created at $createdAt")
        }
        return workoutLogRepo.findByCreatedAt(createdAt);
    }

    fun getWorkoutLogsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): List<WorkoutLog> {
        return workoutLogRepo.findByCreatedAtBetween(startDate, endDate);
    }

    fun getWorkOutLogsByMonth(mon: Mon): List<WorkoutLog> {
        return workoutLogRepo.findWorkoutLogsByMon(mon)
    }

    fun getLatestWorkoutLogs(date: LocalDateTime): List<WorkoutLog> {
        return workoutLogRepo.findFiveLatestWorkoutLogs(date);
    }

    fun getWorkoutLogsByTrainingGoal(trainingGoal: TrainingGoal): List<WorkoutLog> {
        return workoutLogRepo.findByTrainingGoal(trainingGoal);
    }

    fun getWorkoutLogByWorkout(workoutId: String): List<WorkoutLog> {
        return workoutLogRepo.findByWorkoutId(workoutId);
    }

    fun getLatestWorkoutLogByWorkoutId(workoutId: String): WorkoutLog {
        return workoutLogRepo.findTopByWorkoutIdOrderByCreatedAtDesc(workoutId)
            .orElseThrow { WorkoutLogNotFoundException("The latest workout log of the workout with the id $workoutId could not be found.") }
    }

    fun createWorkoutLog(workoutLog: WorkoutLog): WorkoutLog {
        workoutLogRepo.save(workoutLog);
        return workoutLogRepo.save(workoutLog);
    }

    fun updateWorkoutLog(workoutLog: WorkoutLog): WorkoutLog {
        val existing = workoutLogRepo.findById(workoutLog.id)
            .orElseThrow{WorkoutLogNotFoundException("No workout log with id: ${workoutLog.id}");}
        existing.createdAt = workoutLog.createdAt;
        existing.trainingGoal = workoutLog.trainingGoal;
        existing.workout = workoutLog.workout;
        existing.comment = workoutLog.comment;
        return workoutLogRepo.save(existing);
    }

    fun deleteWorkoutLog(id: String) {
        if (!workoutLogRepo.existsById(id)) {
            throw WorkoutLogNotFoundException("No exercise with id: $id");
        }
        return workoutLogRepo.deleteWorkoutLogById(id);
    }
}