package com.tjost.workouttracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "workout_plan_items")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkoutPlanItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_item_id")
    @Setter(AccessLevel.NONE)
    private Long planItemId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "plan_id", nullable = false)
    private WorkoutPlan workoutPlan;

    @ManyToOne(optional = false)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(name = "exercise_position", nullable = false)
    private short exercisePosition;

    @Column(name = "target_weight_kg", nullable = false)
    private BigDecimal targetWeightKg;

    @Column(name = "target_rep_min", nullable = false)
    private short targetRepMin;

    @Column(name = "target_rep_max", nullable = false)
    private short targetRepMax;

    private String comment;

    @Builder
    public WorkoutPlanItem(
            WorkoutPlan workoutPlan,
            Exercise exercise,
            short exercisePosition,
            BigDecimal targetWeightKg,
            short targetRepMin,
            short targetRepMax,
            String comment
    ) {
        this.workoutPlan = workoutPlan;
        this.exercise = exercise;
        this.exercisePosition = exercisePosition;
        this.targetWeightKg = targetWeightKg;
        this.targetRepMin = targetRepMin;
        this.targetRepMax = targetRepMax;
        this.comment = comment;
    }
}