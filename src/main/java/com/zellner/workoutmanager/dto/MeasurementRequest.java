package com.zellner.workoutmanager.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record MeasurementRequest(
        @NotBlank @Size(max = 50) String name,
        @NotNull @Positive Double measuredValue,
        @PastOrPresent LocalDateTime measuredAt) {
}
