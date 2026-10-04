package com.zellner.workoutmanager.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zellner.workoutmanager.entity.Measurement;
import com.zellner.workoutmanager.entity.User;
import com.zellner.workoutmanager.exception.ResourceNotFoundException;
import com.zellner.workoutmanager.repository.MeasurementRepository;

@Service
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final UserService userService;

    public MeasurementService(MeasurementRepository measurementRepository, UserService userService) {
        this.measurementRepository = measurementRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<Measurement> findByOwner(Long ownerId) {
        userService.findById(ownerId);
        return measurementRepository.findByOwnerIdOrderByMeasuredAtDesc(ownerId);
    }

    @Transactional(readOnly = true)
    public Measurement findById(Long id) {
        return measurementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Measurement", id));
    }

    @Transactional
    public Measurement create(Long ownerId, String name, Double measuredValue, LocalDateTime measuredAt) {
        User owner = userService.findById(ownerId);
        Measurement measurement = new Measurement();
        measurement.setOwner(owner);
        measurement.setName(name);
        measurement.setMeasuredValue(measuredValue);
        measurement.setMeasuredAt(measuredAt != null ? measuredAt : LocalDateTime.now());
        return measurementRepository.save(measurement);
    }

    @Transactional
    public Measurement update(Long id, String name, Double measuredValue, LocalDateTime measuredAt) {
        Measurement measurement = findById(id);
        measurement.setName(name);
        measurement.setMeasuredValue(measuredValue);
        measurement.setMeasuredAt(measuredAt);
        return measurement;
    }

    @Transactional
    public void delete(Long id) {
        measurementRepository.delete(findById(id));
    }

}
