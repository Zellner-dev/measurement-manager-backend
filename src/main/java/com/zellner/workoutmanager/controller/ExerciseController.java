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

import com.zellner.workoutmanager.dto.ExerciseRequest;
import com.zellner.workoutmanager.dto.ExerciseResponse;
import com.zellner.workoutmanager.service.ExerciseService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Exercises")
@RestController
@RequestMapping("/api")
public class ExerciseController {

    private final ExerciseService service;

    public ExerciseController(ExerciseService service) {
        this.service = service;
    }

    @GetMapping("/workouts/{workoutId}/exercises")
    public List<ExerciseResponse> findByWorkout(@PathVariable Long workoutId) {
        return service.findByWorkout(workoutId).stream().map(ExerciseResponse::from).toList();
    }

    @PostMapping("/workouts/{workoutId}/exercises")
    public ResponseEntity<ExerciseResponse> create(
            @PathVariable Long workoutId, @Valid @RequestBody ExerciseRequest request) {
        ExerciseResponse response = ExerciseResponse.from(service.create(workoutId, request.name()));
        return ResponseEntity.created(URI.create("/api/exercises/" + response.id())).body(response);
    }

    @GetMapping("/exercises/{id}")
    public ExerciseResponse findById(@PathVariable Long id) {
        return ExerciseResponse.from(service.findById(id));
    }

    @PutMapping("/exercises/{id}")
    public ExerciseResponse update(@PathVariable Long id, @Valid @RequestBody ExerciseRequest request) {
        return ExerciseResponse.from(service.update(id, request.name()));
    }

    @DeleteMapping("/exercises/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
