package com.zellner.workoutmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zellner.workoutmanager.entity.Exercise;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    List<Exercise> findByWorkoutId(Long workoutId);

}
