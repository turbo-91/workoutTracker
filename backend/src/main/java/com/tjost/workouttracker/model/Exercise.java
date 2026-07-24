package com.tjost.workouttracker.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exercises")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_id")
    @Setter(AccessLevel.NONE)
    private Long exerciseId;

    @Column(nullable = false)
    private String name;

    @Builder
    public Exercise(String name) {
        this.name = name;
    }
}