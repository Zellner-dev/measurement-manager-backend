package com.zellner.workoutmanager.dto;

import java.time.LocalDateTime;

import com.zellner.workoutmanager.entity.Measurement;

public record MeasurementResponse(
        Long id, String name, Double measuredValue, LocalDateTime measuredAt, Long ownerId) {

    public static MeasurementResponse from(Measurement measurement) {
        return new MeasurementResponse(
                measurement.getId(),
                measurement.getName(),
                measurement.getMeasuredValue(),
                measurement.getMeasuredAt(),
                measurement.getOwner().getId());
    }

}
