package com.zellner.workoutmanager.dto;

import jakarta.validation.constraints.PositiveOrZero;

public record ExerciseLogUpdateRequest(@PositiveOrZero Double weight) {
}
