package com.zellner.workoutmanager.dto;

import com.zellner.workoutmanager.entity.ExerciseLog;

public record ExerciseLogResponse(Long id, Double weight, Long workoutLogId, Long exerciseId) {

    public static ExerciseLogResponse from(ExerciseLog exerciseLog) {
        return new ExerciseLogResponse(
                exerciseLog.getId(),
                exerciseLog.getWeight(),
                exerciseLog.getWorkoutLog().getId(),
                exerciseLog.getExercise().getId());
    }

}
