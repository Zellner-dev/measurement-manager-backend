package com.zellner.workoutmanager.service;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("No authenticated user");
        }
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Authenticated user not found"));
    }

    /**
     * Users can only access their own data. Resources owned by someone else are reported as not found,
     * so the API does not reveal that they exist.
     */
    public void checkOwner(User owner, String resource, Long id) {
        if (!owner.getId().equals(getCurrentUser().getId())) {
            throw new ResourceNotFoundException(resource, id);
        }
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        User currentUser = getCurrentUser();
        if (!currentUser.getId().equals(id)) {
            throw new ResourceNotFoundException("User", id);
        }
        return currentUser;
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
