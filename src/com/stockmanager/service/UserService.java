package com.stockmanager.service;

import com.stockmanager.model.Role;
import com.stockmanager.model.User;
import com.stockmanager.repository.UserRepository;

import java.util.List;

public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(String email, String password, Role role) {
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
}