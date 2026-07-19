package com.tjost.workouttracker;

import jakarta.persistence.*;

@Entity
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_id")
    private Long id;

    private String name;

    protected Exercise() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}