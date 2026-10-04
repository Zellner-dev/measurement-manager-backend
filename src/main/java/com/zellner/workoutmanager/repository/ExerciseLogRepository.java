package com.zellner.workoutmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zellner.workoutmanager.entity.ExerciseLog;

public interface ExerciseLogRepository extends JpaRepository<ExerciseLog, Long> {

    List<ExerciseLog> findByWorkoutLogId(Long workoutLogId);

    List<ExerciseLog> findByExerciseId(Long exerciseId);

}
