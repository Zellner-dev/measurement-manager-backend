package com.zellner.workoutmanager.dto;

import java.time.LocalDateTime;

import com.zellner.workoutmanager.entity.WorkoutLog;

public record WorkoutLogResponse(Long id, LocalDateTime performedAt, Long workoutId) {

    public static WorkoutLogResponse from(WorkoutLog workoutLog) {
        return new WorkoutLogResponse(
                workoutLog.getId(), workoutLog.getPerformedAt(), workoutLog.getWorkout().getId());
    }

}
