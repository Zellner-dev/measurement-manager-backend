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

import com.zellner.workoutmanager.dto.MeasurementRequest;
import com.zellner.workoutmanager.dto.MeasurementResponse;
import com.zellner.workoutmanager.service.MeasurementService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Measurements")
@RestController
@RequestMapping("/api")
public class MeasurementController {

    private final MeasurementService service;

    public MeasurementController(MeasurementService service) {
        this.service = service;
    }

    @GetMapping("/users/{userId}/measurements")
    public List<MeasurementResponse> findByOwner(@PathVariable Long userId) {
        return service.findByOwner(userId).stream().map(MeasurementResponse::from).toList();
    }

    @PostMapping("/users/{userId}/measurements")
    public ResponseEntity<MeasurementResponse> create(
            @PathVariable Long userId, @Valid @RequestBody MeasurementRequest request) {
        MeasurementResponse response = MeasurementResponse.from(
                service.create(userId, request.name(), request.measuredValue(), request.measuredAt()));
        return ResponseEntity.created(URI.create("/api/measurements/" + response.id())).body(response);
    }

    @GetMapping("/measurements/{id}")
    public MeasurementResponse findById(@PathVariable Long id) {
        return MeasurementResponse.from(service.findById(id));
    }

    @PutMapping("/measurements/{id}")
    public MeasurementResponse update(@PathVariable Long id, @Valid @RequestBody MeasurementRequest request) {
        return MeasurementResponse.from(
                service.update(id, request.name(), request.measuredValue(), request.measuredAt()));
    }

    @DeleteMapping("/measurements/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
