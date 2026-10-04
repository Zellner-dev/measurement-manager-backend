package com.zellner.workoutmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zellner.workoutmanager.entity.Workout;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    List<Workout> findByOwnerId(Long ownerId);

}
