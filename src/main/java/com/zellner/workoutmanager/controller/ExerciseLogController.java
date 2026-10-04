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

import com.zellner.workoutmanager.dto.ExerciseLogCreateRequest;
import com.zellner.workoutmanager.dto.ExerciseLogUpdateRequest;
import com.zellner.workoutmanager.dto.ExerciseLogResponse;
import com.zellner.workoutmanager.service.ExerciseLogService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Exercise logs")
@RestController
@RequestMapping("/api")
public class ExerciseLogController {

    private final ExerciseLogService service;

    public ExerciseLogController(ExerciseLogService service) {
        this.service = service;
    }

    @GetMapping("/workout-logs/{workoutLogId}/exercise-logs")
    public List<ExerciseLogResponse> findByWorkoutLog(@PathVariable Long workoutLogId) {
        return service.findByWorkoutLog(workoutLogId).stream().map(ExerciseLogResponse::from).toList();
    }

    @PostMapping("/workout-logs/{workoutLogId}/exercise-logs")
    public ResponseEntity<ExerciseLogResponse> create(
            @PathVariable Long workoutLogId, @Valid @RequestBody ExerciseLogCreateRequest request) {
        ExerciseLogResponse response = ExerciseLogResponse.from(
                service.create(workoutLogId, request.exerciseId(), request.weight()));
        return ResponseEntity.created(URI.create("/api/exercise-logs/" + response.id())).body(response);
    }

    @GetMapping("/exercise-logs/{id}")
    public ExerciseLogResponse findById(@PathVariable Long id) {
        return ExerciseLogResponse.from(service.findById(id));
    }

    @PutMapping("/exercise-logs/{id}")
    public ExerciseLogResponse update(@PathVariable Long id, @Valid @RequestBody ExerciseLogUpdateRequest request) {
        return ExerciseLogResponse.from(service.update(id, request.weight()));
    }

    @DeleteMapping("/exercise-logs/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
