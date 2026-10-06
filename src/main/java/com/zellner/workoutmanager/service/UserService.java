package com.zellner.workoutmanager.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zellner.workoutmanager.entity.User;
import com.zellner.workoutmanager.exception.EmailAlreadyInUseException;
import com.zellner.workoutmanager.exception.ResourceNotFoundException;
import com.zellner.workoutmanager.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    @Transactional
    public User create(String name, String email, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyInUseException(email);
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        return userRepository.save(user);
    }

    @Transactional
    public User update(Long id, String name, String email) {
        User user = findById(id);
        if (!user.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            throw new EmailAlreadyInUseException(email);
        }
        user.setName(name);
        user.setEmail(email);
        return user;
    }

    @Transactional
    public void changePassword(Long id, String rawPassword) {
        User user = findById(id);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
    }

    @Transactional
    public void delete(Long id) {
        userRepository.delete(findById(id));
    }

}
