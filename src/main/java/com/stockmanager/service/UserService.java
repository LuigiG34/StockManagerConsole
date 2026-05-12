package com.stockmanager.service;

import java.util.List;

import com.stockmanager.model.Role;
import com.stockmanager.model.User;
import com.stockmanager.repository.UserRepository;

public class UserService {
    private UserRepository userRepository;
    private AuthService authService;

    public UserService(UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    public List<User> getAllUsers() {
        if (!currentUserIsAdmin()) {
            return List.of();
        }

        return userRepository.findAll();
    }

    public User createUser(String email, String password, Role role) {
        if (!currentUserIsAdmin()) {
            return null;
        }

        if (email == null || email.isBlank()) {
            return null;
        }

        if (password == null || password.isBlank()) {
            return null;
        }

        if (role == null) {
            return null;
        }

        if (userRepository.existsByEmail(email)) {
            return null;
        }

        return userRepository.create(email, password, role);
    }

    private boolean currentUserIsAdmin() {
        User currentUser = authService.getCurrentUser();

        if (currentUser == null) {
            return false;
        }

        return currentUser.isAdmin();
    }
}