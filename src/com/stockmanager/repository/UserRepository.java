package com.stockmanager.repository;

import com.stockmanager.model.Role;
import com.stockmanager.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private List<User> users = new ArrayList<>();
    private int nextId = 1;

    public UserRepository() {
        save(new User(nextId++, "admin@test.com", "admin123", Role.ADMIN));
        save(new User(nextId++, "user@test.com", "user123", Role.USER));
    }

    public List<User> findAll() {
        return users;
    }

    public User findByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }

        return null;
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email) != null;
    }

    public User save(User user) {
        users.add(user);
        return user;
    }

    public User create(String email, String password, Role role) {
        User user = new User(nextId++, email, password, role);
        users.add(user);

        return user;
    }
}