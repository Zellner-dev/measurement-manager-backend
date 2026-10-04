package com.zellner.workoutmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record WorkoutRequest(@NotBlank @Size(max = 100) String name) {
}
