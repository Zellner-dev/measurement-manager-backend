package com.zellner.workoutmanager.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ExerciseLogCreateRequest(@NotNull Long exerciseId, @PositiveOrZero Double weight) {
}
