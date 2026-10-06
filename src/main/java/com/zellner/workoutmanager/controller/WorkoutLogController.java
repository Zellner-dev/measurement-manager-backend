package com.zellner.workoutmanager.controller;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zellner.workoutmanager.dto.WorkoutLogRequest;
import com.zellner.workoutmanager.dto.WorkoutLogResponse;
import com.zellner.workoutmanager.service.WorkoutLogService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Workout logs")
@RestController
@RequestMapping("/api")
public class WorkoutLogController {

    private final WorkoutLogService service;

    public WorkoutLogController(WorkoutLogService service) {
        this.service = service;
    }

    @GetMapping("/workouts/{workoutId}/logs")
    public List<WorkoutLogResponse> findByWorkout(@PathVariable Long workoutId) {
        return service.findByWorkout(workoutId).stream().map(WorkoutLogResponse::from).toList();
    }

    @GetMapping("/users/{userId}/workout-logs")
    public List<WorkoutLogResponse> findByOwner(@PathVariable Long userId) {
        return service.findByOwner(userId).stream().map(WorkoutLogResponse::from).toList();
    }

    @PostMapping("/workouts/{workoutId}/logs")
    public ResponseEntity<WorkoutLogResponse> create(
            @PathVariable Long workoutId, @Valid @RequestBody WorkoutLogRequest request) {
        WorkoutLogResponse response = WorkoutLogResponse.from(service.create(workoutId, request.performedAt()));
        return ResponseEntity.created(URI.create("/api/workout-logs/" + response.id())).body(response);
    }

    @GetMapping("/workout-logs/{id}")
    public WorkoutLogResponse findById(@PathVariable Long id) {
        return WorkoutLogResponse.from(service.findById(id));
    }

    @PutMapping("/workout-logs/{id}")
    public WorkoutLogResponse update(@PathVariable Long id, @Valid @RequestBody WorkoutLogRequest request) {
        return WorkoutLogResponse.from(service.update(id, request.performedAt()));
    }

    @DeleteMapping("/workout-logs/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
