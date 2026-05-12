package com.stockmanager.service;

import com.stockmanager.model.User;
import com.stockmanager.repository.UserRepository;

public class AuthService {
    private UserRepository userRepository;
    private User currentUser;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean login(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return false;
        }

        if (!user.checkPassword(password)) {
            return false;
        }

        currentUser = user;
        return true;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}