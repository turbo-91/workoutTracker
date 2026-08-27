package com.tjost.workouttracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "warm_up_sets")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WarmUp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warm_up_set_id")
    @Setter(AccessLevel.NONE)
    private Long warmUpSetId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "plan_item_id", nullable = false)
    private WorkoutPlanItem workoutPlanItem;

    @Column(name = "set_number",  nullable = false)
    private short setNumber;

    @Column(name = "weight_kg", nullable = false)
    private BigDecimal weightKg;

    @Column(name = "rep_min", nullable = false)
    private short repMin;

    @Column(name = "rep_max", nullable = false)
    private short repMax;

    @Column(name = "rest_seconds", nullable = false)
    private short restSeconds;

    @Builder
    public WarmUp(
            WorkoutPlanItem workoutPlanItem,
            short setNumber,
            BigDecimal weightKg,
            short repMin,
            short repMax,
            short restSeconds
    ) {
        this.workoutPlanItem = workoutPlanItem;
        this.setNumber = setNumber;
        this.weightKg = weightKg;
        this.repMin = repMin;
        this.repMax = repMax;
        this.restSeconds = restSeconds;
    }
}
