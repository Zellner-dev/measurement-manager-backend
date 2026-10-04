package com.zellner.workoutmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zellner.workoutmanager.entity.WorkoutLog;

public interface WorkoutLogRepository extends JpaRepository<WorkoutLog, Long> {

    List<WorkoutLog> findByWorkoutIdOrderByPerformedAtDesc(Long workoutId);

}
