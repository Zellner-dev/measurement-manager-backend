package com.zellner.workoutmanager.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zellner.workoutmanager.entity.Exercise;
import com.zellner.workoutmanager.entity.ExerciseLog;
import com.zellner.workoutmanager.entity.WorkoutLog;
import com.zellner.workoutmanager.exception.ResourceNotFoundException;
import com.zellner.workoutmanager.repository.ExerciseLogRepository;

@Service
public class ExerciseLogService {

    private final ExerciseLogRepository exerciseLogRepository;
    private final WorkoutLogService workoutLogService;
    private final ExerciseService exerciseService;
    private final UserService userService;

    public ExerciseLogService(
            ExerciseLogRepository exerciseLogRepository,
            WorkoutLogService workoutLogService,
            ExerciseService exerciseService,
            UserService userService) {
        this.exerciseLogRepository = exerciseLogRepository;
        this.workoutLogService = workoutLogService;
        this.exerciseService = exerciseService;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<ExerciseLog> findByWorkoutLog(Long workoutLogId) {
        workoutLogService.findById(workoutLogId);
        return exerciseLogRepository.findByWorkoutLogId(workoutLogId);
    }

    @Transactional(readOnly = true)
    public List<ExerciseLog> findByExercise(Long exerciseId) {
        exerciseService.findById(exerciseId);
        return exerciseLogRepository.findByExerciseId(exerciseId);
    }

    @Transactional(readOnly = true)
    public ExerciseLog findById(Long id) {
        ExerciseLog exerciseLog = exerciseLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ExerciseLog", id));
        userService.checkOwner(exerciseLog.getWorkoutLog().getWorkout().getOwner(), "ExerciseLog", id);
        return exerciseLog;
    }

    @Transactional
    public ExerciseLog create(Long workoutLogId, Long exerciseId, Double weight) {
        WorkoutLog workoutLog = workoutLogService.findById(workoutLogId);
        Exercise exercise = exerciseService.findById(exerciseId);
        if (!exercise.getWorkout().getId().equals(workoutLog.getWorkout().getId())) {
            throw new IllegalArgumentException("Exercise " + exerciseId + " does not belong to the logged workout");
        }
        ExerciseLog exerciseLog = new ExerciseLog();
        exerciseLog.setWorkoutLog(workoutLog);
        exerciseLog.setExercise(exercise);
        exerciseLog.setWeight(weight);
        return exerciseLogRepository.save(exerciseLog);
    }

    @Transactional
    public ExerciseLog update(Long id, Double weight) {
        ExerciseLog exerciseLog = findById(id);
        exerciseLog.setWeight(weight);
        return exerciseLog;
    }

    @Transactional
    public void delete(Long id) {
        exerciseLogRepository.delete(findById(id));
    }

}
