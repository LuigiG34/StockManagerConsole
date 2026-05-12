package com.stockmanager.service;

import com.stockmanager.exception.DuplicateEmailException;
import com.stockmanager.exception.UnauthorizedException;
import com.stockmanager.exception.ValidationException;
import com.stockmanager.model.Role;
import com.stockmanager.model.User;
import com.stockmanager.repository.UserRepository;

import java.util.List;

public class UserService {
    private UserRepository userRepository;
    private AuthService authService;

    public UserService(UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    public List<User> getAllUsers() {
        requireAdmin();

        return userRepository.findAll();
    }

    public User createUser(String email, String password, Role role) {
        requireAdmin();

        if (email == null || email.isBlank()) {
            throw new ValidationException("L'email ne peut pas être vide.");
        }

        if (password == null || password.isBlank()) {
            throw new ValidationException("Le mot de passe ne peut pas être vide.");
        }

        if (role == null) {
            throw new ValidationException("Le rôle est obligatoire.");
        }

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Cet email est déjà utilisé.");
        }

        return userRepository.create(email, password, role);
    }

    private void requireAdmin() {
        User currentUser = authService.getCurrentUser();

        if (currentUser == null || !currentUser.isAdmin()) {
            throw new UnauthorizedException("Action réservée aux administrateurs.");
        }
    }
}