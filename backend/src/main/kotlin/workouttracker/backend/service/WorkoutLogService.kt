package workouttracker.backend.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import workouttracker.backend.exception.WorkoutLogNotFoundException
import workouttracker.backend.model.WorkoutLog
import workouttracker.backend.model.enums.TrainingGoal
import workouttracker.backend.repository.WorkoutLogRepo
import java.time.LocalDateTime
import java.util.*
import kotlin.NoSuchElementException

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

    fun getWorkoutLogByTrainingGoal(trainingGoal: TrainingGoal): List<WorkoutLog> {
        return workoutLogRepo.findByTrainingGoal(trainingGoal);
    }

    fun getWorkoutLogByWorkout(workoutId: String): List<WorkoutLog> {
        return workoutLogRepo.findByWorkoutId(workoutId);
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