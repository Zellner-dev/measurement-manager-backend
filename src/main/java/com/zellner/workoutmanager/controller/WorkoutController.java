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

import com.zellner.workoutmanager.dto.WorkoutRequest;
import com.zellner.workoutmanager.dto.WorkoutResponse;
import com.zellner.workoutmanager.service.WorkoutService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Workouts")
@RestController
@RequestMapping("/api")
public class WorkoutController {

    private final WorkoutService service;

    public WorkoutController(WorkoutService service) {
        this.service = service;
    }

    @GetMapping("/users/{userId}/workouts")
    public List<WorkoutResponse> findByOwner(@PathVariable Long userId) {
        return service.findByOwner(userId).stream().map(WorkoutResponse::from).toList();
    }

    @PostMapping("/users/{userId}/workouts")
    public ResponseEntity<WorkoutResponse> create(
            @PathVariable Long userId, @Valid @RequestBody WorkoutRequest request) {
        WorkoutResponse response = WorkoutResponse.from(service.create(userId, request.name()));
        return ResponseEntity.created(URI.create("/api/workouts/" + response.id())).body(response);
    }

    @GetMapping("/workouts/{id}")
    public WorkoutResponse findById(@PathVariable Long id) {
        return WorkoutResponse.from(service.findById(id));
    }

    @PutMapping("/workouts/{id}")
    public WorkoutResponse update(@PathVariable Long id, @Valid @RequestBody WorkoutRequest request) {
        return WorkoutResponse.from(service.update(id, request.name()));
    }

    @DeleteMapping("/workouts/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
