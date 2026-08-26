package com.tjost.workouttracker.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "workout_logs")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkoutLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id", nullable = false)
    private Long logId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "plan_id", nullable = false)
    private WorkoutPlan workoutPlan;

    @Column(name = "workout_date", nullable = false)
    private LocalDate workoutDate;

    private String comment;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Builder
    public WorkoutLog(
            Long logId,
            WorkoutPlan workoutPlan,
            LocalDate workoutDate,
            String comment,
            OffsetDateTime createdAt
    ) {
        this.logId = logId;
        this.workoutPlan = workoutPlan;
        this.workoutDate = workoutDate;
        this.comment = comment;
        this.createdAt = createdAt;
    }
}
