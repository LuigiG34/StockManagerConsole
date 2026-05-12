package com.stockmanager.service;

import com.stockmanager.exception.DuplicateEmailException;
import com.stockmanager.exception.UnauthorizedException;
import com.stockmanager.exception.ValidationException;
import com.stockmanager.model.Role;
import com.stockmanager.model.User;
import com.stockmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    @TempDir
    Path tempDir;

    @Test
    void adminShouldListUsers() {
        UserRepository userRepository = new UserRepository(tempDir.resolve("users.csv"));

        AuthService authService = new AuthService(userRepository);
        authService.login("admin@test.com", "admin123");

        UserService userService = new UserService(userRepository, authService);

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("admin@test.com", users.get(0).getEmail());
    }

    @Test
    void userShouldNotListUsers() {
        UserRepository userRepository = new UserRepository(tempDir.resolve("users.csv"));

        AuthService authService = new AuthService(userRepository);
        authService.login("user@test.com", "user123");

        UserService userService = new UserService(userRepository, authService);

        assertThrows(
                UnauthorizedException.class,
                userService::getAllUsers
        );
    }

    @Test
    void adminShouldCreateUser() {
        UserRepository userRepository = new UserRepository(tempDir.resolve("users.csv"));

        AuthService authService = new AuthService(userRepository);
        authService.login("admin@test.com", "admin123");

        UserService userService = new UserService(userRepository, authService);

        User user = userService.createUser("test@test.com", "test123", Role.USER);

        assertNotNull(user);
        assertEquals("test@test.com", user.getEmail());
        assertEquals(Role.USER, user.getRole());
        assertEquals(3, userService.getAllUsers().size());
    }

    @Test
    void userShouldNotCreateUser() {
        UserRepository userRepository = new UserRepository(tempDir.resolve("users.csv"));

        AuthService authService = new AuthService(userRepository);
        authService.login("user@test.com", "user123");

        UserService userService = new UserService(userRepository, authService);

        assertThrows(
                UnauthorizedException.class,
                () -> userService.createUser("test@test.com", "test123", Role.USER)
        );
    }

    @Test
    void emailShouldBeUnique() {
        UserRepository userRepository = new UserRepository(tempDir.resolve("users.csv"));

        AuthService authService = new AuthService(userRepository);
        authService.login("admin@test.com", "admin123");

        UserService userService = new UserService(userRepository, authService);

        assertThrows(
                DuplicateEmailException.class,
                () -> userService.createUser("user@test.com", "password", Role.USER)
        );
    }

    @Test
    void emailShouldNotBeBlank() {
        UserRepository userRepository = new UserRepository(tempDir.resolve("users.csv"));

        AuthService authService = new AuthService(userRepository);
        authService.login("admin@test.com", "admin123");

        UserService userService = new UserService(userRepository, authService);

        assertThrows(
                ValidationException.class,
                () -> userService.createUser("", "password", Role.USER)
        );
    }

    @Test
    void passwordShouldNotBeBlank() {
        UserRepository userRepository = new UserRepository(tempDir.resolve("users.csv"));

        AuthService authService = new AuthService(userRepository);
        authService.login("admin@test.com", "admin123");

        UserService userService = new UserService(userRepository, authService);

        assertThrows(
                ValidationException.class,
                () -> userService.createUser("test@test.com", "", Role.USER)
        );
    }

    @Test
    void roleShouldNotBeNull() {
        UserRepository userRepository = new UserRepository(tempDir.resolve("users.csv"));

        AuthService authService = new AuthService(userRepository);
        authService.login("admin@test.com", "admin123");

        UserService userService = new UserService(userRepository, authService);

        assertThrows(
                ValidationException.class,
                () -> userService.createUser("test@test.com", "test123", null)
        );
    }
}