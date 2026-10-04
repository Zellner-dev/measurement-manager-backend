package com.zellner.workoutmanager.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.PastOrPresent;

public record WorkoutLogRequest(@PastOrPresent LocalDateTime performedAt) {
}
