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

    public WorkoutLogService(WorkoutLogRepository workoutLogRepository, WorkoutService workoutService) {
        this.workoutLogRepository = workoutLogRepository;
        this.workoutService = workoutService;
    }

    @Transactional(readOnly = true)
    public List<WorkoutLog> findByWorkout(Long workoutId) {
        workoutService.findById(workoutId);
        return workoutLogRepository.findByWorkoutIdOrderByPerformedAtDesc(workoutId);
    }

    @Transactional(readOnly = true)
    public WorkoutLog findById(Long id) {
        return workoutLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WorkoutLog", id));
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
        workoutLog.setPerformedAt(performedAt);
        return workoutLog;
    }

    @Transactional
    public void delete(Long id) {
        workoutLogRepository.delete(findById(id));
    }

}
