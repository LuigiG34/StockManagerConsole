package com.stockmanager.service;

import com.stockmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {
    @TempDir
    Path tempDir;

    @Test
    void loginShouldSucceedWithValidCredentials() {
        UserRepository userRepository = new UserRepository(tempDir.resolve("users.csv"));
        AuthService authService = new AuthService(userRepository);

        boolean success = authService.login("admin@test.com", "admin123");

        assertTrue(success);
        assertTrue(authService.isLoggedIn());
        assertEquals("admin@test.com", authService.getCurrentUser().getEmail());
    }

    @Test
    void loginShouldFailWithInvalidPassword() {
        UserRepository userRepository = new UserRepository(tempDir.resolve("users.csv"));
        AuthService authService = new AuthService(userRepository);

        boolean success = authService.login("admin@test.com", "wrong");

        assertFalse(success);
        assertFalse(authService.isLoggedIn());
        assertNull(authService.getCurrentUser());
    }

    @Test
    void loginShouldFailWithUnknownEmail() {
        UserRepository userRepository = new UserRepository(tempDir.resolve("users.csv"));
        AuthService authService = new AuthService(userRepository);

        boolean success = authService.login("unknown@test.com", "admin123");

        assertFalse(success);
        assertFalse(authService.isLoggedIn());
        assertNull(authService.getCurrentUser());
    }

    @Test
    void logoutShouldRemoveCurrentUser() {
        UserRepository userRepository = new UserRepository(tempDir.resolve("users.csv"));
        AuthService authService = new AuthService(userRepository);

        authService.login("admin@test.com", "admin123");
        authService.logout();

        assertFalse(authService.isLoggedIn());
        assertNull(authService.getCurrentUser());
    }
}