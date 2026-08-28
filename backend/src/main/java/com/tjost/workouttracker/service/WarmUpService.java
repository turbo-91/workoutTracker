package com.tjost.workouttracker.service;

import com.tjost.workouttracker.dto.WarmUpRequest;
import com.tjost.workouttracker.exception.ItemNotFoundException;
import com.tjost.workouttracker.exception.WarmUpAlreadyExistsException;
import com.tjost.workouttracker.exception.WarmUpNotFoundException;
import com.tjost.workouttracker.exception.WarmUpNotFoundExceptionPlanItemId;
import com.tjost.workouttracker.model.WarmUp;
import com.tjost.workouttracker.model.WorkoutPlanItem;
import com.tjost.workouttracker.repository.WarmUpRepository;
import com.tjost.workouttracker.repository.WorkoutPlanItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WarmUpService {

    private final WarmUpRepository warmUpRepo;
    private final WorkoutPlanItemRepository itemRepo;

    public WarmUp createWarmUp (
            Long planItemId,
            short setNumber,
            BigDecimal weightKg,
            short repMin,
            short repMax,
            short restSeconds) {
        WorkoutPlanItem item = itemRepo.findById(planItemId)
                .orElseThrow(() -> new ItemNotFoundException(planItemId));

        if (warmUpRepo.existsByWorkoutPlanItemId(planItemId)) {
            throw new WarmUpAlreadyExistsException(planItemId);
        }

        WarmUp warmUp = WarmUp.builder()
                .workoutPlanItem(item)
                .setNumber(setNumber)
                .weightKg(weightKg)
                .repMin(repMin)
                .repMax(repMax)
                .restSeconds(restSeconds)
                .build();

        return warmUpRepo.save(warmUp);
    };

    public WarmUp getWarmUpById(Long warmUpId){
        return warmUpRepo.findById(warmUpId)
                .orElseThrow(() -> new WarmUpNotFoundException(warmUpId));
    }

    public WarmUp getWarmUpByPlanItemId(Long planItemId) {
        return warmUpRepo.findByWorkoutPlanItemId(planItemId)
                .orElseThrow(() -> new WarmUpNotFoundExceptionPlanItemId(planItemId));
    }

    @Transactional
    public WarmUp updateWarmUp(Long warmUpId, WarmUpRequest request) {
        WarmUp existingWarmUp = warmUpRepo.findById(warmUpId)
                .orElseThrow(() -> new WarmUpNotFoundException(warmUpId));

        WorkoutPlanItem item = itemRepo.findById(request.planItemId())
                .orElseThrow(() -> new ItemNotFoundException(request.planItemId()));

        existingWarmUp.setWorkoutPlanItem(item);
        existingWarmUp.setSetNumber(request.setNumber());
        existingWarmUp.setWeightKg(request.weightKg());
        existingWarmUp.setRepMin(request.repMin());
        existingWarmUp.setRepMax(request.repMax());
        existingWarmUp.setRestSeconds(request.restSeconds());

        return existingWarmUp;
    }

    @Transactional
    public void deleteWarmUp(Long warmUpId) {
        WarmUp warmUp = warmUpRepo.findById(warmUpId)
                .orElseThrow(() -> new WarmUpNotFoundException(warmUpId));
        warmUpRepo.delete(warmUp);
    }

}
