package com.zellner.workoutmanager.controller;

import java.net.URI;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zellner.workoutmanager.dto.PasswordChangeRequest;
import com.zellner.workoutmanager.dto.UserCreateRequest;
import com.zellner.workoutmanager.dto.UserResponse;
import com.zellner.workoutmanager.dto.UserUpdateRequest;
import com.zellner.workoutmanager.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Users")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserResponse me() {
        return UserResponse.from(userService.getCurrentUser());
    }

    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable Long id) {
        return UserResponse.from(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserCreateRequest request) {
        UserResponse response = UserResponse.from(
                userService.create(request.name(), request.email(), request.password()));
        return ResponseEntity.created(URI.create("/api/users/" + response.id())).body(response);
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
        return UserResponse.from(userService.update(id, request.name(), request.email()));
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id, @Valid @RequestBody PasswordChangeRequest request) {
        userService.changePassword(id, request.password());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
