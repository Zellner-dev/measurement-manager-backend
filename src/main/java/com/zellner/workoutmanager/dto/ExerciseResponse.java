package com.zellner.workoutmanager.dto;

import com.zellner.workoutmanager.entity.Exercise;

public record ExerciseResponse(Long id, String name, Long workoutId) {

    public static ExerciseResponse from(Exercise exercise) {
        return new ExerciseResponse(exercise.getId(), exercise.getName(), exercise.getWorkout().getId());
    }

}
