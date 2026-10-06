package com.zellner.workoutmanager.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zellner.workoutmanager.entity.Workout;
import com.zellner.workoutmanager.entity.WorkoutLog;
import com.zellner.workoutmanager.exception.ResourceNotFoundException;
import com.zellner.workoutmanager.repository.WorkoutLogRepository;

@Service
public class WorkoutLogService {

    private final WorkoutLogRepository workoutLogRepository;
    private final WorkoutService workoutService;
    private final UserService userService;

    public WorkoutLogService(
            WorkoutLogRepository workoutLogRepository, WorkoutService workoutService, UserService userService) {
        this.workoutLogRepository = workoutLogRepository;
        this.workoutService = workoutService;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<WorkoutLog> findByWorkout(Long workoutId) {
        workoutService.findById(workoutId);
        return workoutLogRepository.findByWorkoutIdOrderByPerformedAtDesc(workoutId);
    }

    @Transactional(readOnly = true)
    public List<WorkoutLog> findByOwner(Long ownerId) {
        userService.findById(ownerId);
        return workoutLogRepository.findByWorkoutOwnerIdOrderByPerformedAtDesc(ownerId);
    }

    @Transactional(readOnly = true)
    public WorkoutLog findById(Long id) {
        WorkoutLog workoutLog = workoutLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WorkoutLog", id));
        userService.checkOwner(workoutLog.getWorkout().getOwner(), "WorkoutLog", id);
        return workoutLog;
    }

    @Transactional
    public WorkoutLog create(Long workoutId, LocalDateTime performedAt) {
        Workout workout = workoutService.findById(workoutId);
        WorkoutLog workoutLog = new WorkoutLog();
        workoutLog.setWorkout(workout);
        workoutLog.setPerformedAt(performedAt != null ? performedAt : LocalDateTime.now());
        return workoutLogRepository.save(workoutLog);
    }

    @Transactional
    public WorkoutLog update(Long id, LocalDateTime performedAt) {
        WorkoutLog workoutLog = findById(id);
        if (performedAt != null) {
            workoutLog.setPerformedAt(performedAt);
        }
        return workoutLog;
    }

    @Transactional
    public void delete(Long id) {
        workoutLogRepository.delete(findById(id));
    }

}
