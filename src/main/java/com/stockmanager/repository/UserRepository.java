package com.stockmanager.repository;

import com.stockmanager.model.Role;
import com.stockmanager.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private List<User> users = new ArrayList<>();
    private int nextId = 1;
    private final Path filePath;

    public UserRepository() {
        this(Path.of("data/users.csv"));
    }

    public UserRepository(Path filePath) {
        this.filePath = filePath;
        loadFromFile();

        if (users.isEmpty()) {
            seedDefaultUsers();
            saveToFile();
        }
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

    public User create(String email, String password, Role role) {
        User user = new User(nextId++, email, password, role);
        users.add(user);
        saveToFile();

        return user;
    }

    private void seedDefaultUsers() {
        users.add(new User(nextId++, "admin@test.com", "admin123", Role.ADMIN));
        users.add(new User(nextId++, "user@test.com", "user123", Role.USER));
    }

    private void loadFromFile() {
        try {
            if (!Files.exists(filePath)) {
                return;
            }

            List<String> lines = Files.readAllLines(filePath);

            for (String line : lines) {
                if (line.isBlank()) {
                    continue;
                }

                String[] parts = line.split(";", -1);

                if (parts.length != 4) {
                    continue;
                }

                int id = Integer.parseInt(parts[0]);
                String email = parts[1];
                String password = parts[2];
                Role role = Role.valueOf(parts[3]);

                users.add(new User(id, email, password, role));

                if (id >= nextId) {
                    nextId = id + 1;
                }
            }
        } catch (IOException | IllegalArgumentException exception) {
            System.out.println("Erreur lors du chargement des utilisateurs : " + exception.getMessage());
        }
    }

    private void saveToFile() {
        try {
            Files.createDirectories(filePath.getParent());

            List<String> lines = new ArrayList<>();

            for (User user : users) {
                String line = user.getId()
                        + ";" + user.getEmail()
                        + ";" + user.getPassword()
                        + ";" + user.getRole();

                lines.add(line);
            }

            Files.write(filePath, lines);
        } catch (IOException exception) {
            System.out.println("Erreur lors de la sauvegarde des utilisateurs : " + exception.getMessage());
        }
    }
}