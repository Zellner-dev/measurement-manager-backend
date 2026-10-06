package com.zellner.workoutmanager.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zellner.workoutmanager.entity.Exercise;
import com.zellner.workoutmanager.entity.Workout;
import com.zellner.workoutmanager.exception.ResourceNotFoundException;
import com.zellner.workoutmanager.repository.ExerciseRepository;

@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final WorkoutService workoutService;
    private final UserService userService;

    public ExerciseService(
            ExerciseRepository exerciseRepository, WorkoutService workoutService, UserService userService) {
        this.exerciseRepository = exerciseRepository;
        this.workoutService = workoutService;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<Exercise> findByWorkout(Long workoutId) {
        workoutService.findById(workoutId);
        return exerciseRepository.findByWorkoutId(workoutId);
    }

    @Transactional(readOnly = true)
    public Exercise findById(Long id) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise", id));
        userService.checkOwner(exercise.getWorkout().getOwner(), "Exercise", id);
        return exercise;
    }

    @Transactional
    public Exercise create(Long workoutId, String name) {
        Workout workout = workoutService.findById(workoutId);
        Exercise exercise = new Exercise();
        exercise.setWorkout(workout);
        exercise.setName(name);
        return exerciseRepository.save(exercise);
    }

    @Transactional
    public Exercise update(Long id, String name) {
        Exercise exercise = findById(id);
        exercise.setName(name);
        return exercise;
    }

    @Transactional
    public void delete(Long id) {
        exerciseRepository.delete(findById(id));
    }

}
