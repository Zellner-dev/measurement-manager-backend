package com.zellner.workoutmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zellner.workoutmanager.entity.Measurement;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    List<Measurement> findByOwnerIdOrderByMeasuredAtDesc(Long ownerId);

}
