package com.zellner.workoutmanager.dto;

import com.zellner.workoutmanager.entity.Workout;

public record WorkoutResponse(Long id, String name, Long ownerId) {

    public static WorkoutResponse from(Workout workout) {
        return new WorkoutResponse(workout.getId(), workout.getName(), workout.getOwner().getId());
    }

}
