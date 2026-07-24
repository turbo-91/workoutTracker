package com.tjost.workouttracker.model;

import com.tjost.workouttracker.model.enums.Day;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "workout_plans")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkoutPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    @Setter(AccessLevel.NONE)
    private Long planId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Day day;

    @Column(nullable = false)
    private String name;

    private String comment;

    @Builder
    public WorkoutPlan(Day day, String name, String comment) {
        this.day = day;
        this.name = name;
        this.comment = comment;
    }
}