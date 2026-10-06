package com.zellner.workoutmanager.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zellner.workoutmanager.entity.User;
import com.zellner.workoutmanager.entity.Workout;
import com.zellner.workoutmanager.exception.ResourceNotFoundException;
import com.zellner.workoutmanager.repository.WorkoutRepository;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserService userService;

    public WorkoutService(WorkoutRepository workoutRepository, UserService userService) {
        this.workoutRepository = workoutRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<Workout> findByOwner(Long ownerId) {
        userService.findById(ownerId);
        return workoutRepository.findByOwnerId(ownerId);
    }

    @Transactional(readOnly = true)
    public Workout findById(Long id) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout", id));
        userService.checkOwner(workout.getOwner(), "Workout", id);
        return workout;
    }

    @Transactional
    public Workout create(Long ownerId, String name) {
        User owner = userService.findById(ownerId);
        Workout workout = new Workout();
        workout.setOwner(owner);
        workout.setName(name);
        return workoutRepository.save(workout);
    }

    @Transactional
    public Workout update(Long id, String name) {
        Workout workout = findById(id);
        workout.setName(name);
        return workout;
    }

    @Transactional
    public void delete(Long id) {
        workoutRepository.delete(findById(id));
    }

}
