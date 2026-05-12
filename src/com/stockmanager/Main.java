package com.stockmanager;

import com.stockmanager.repository.ProductRepository;
import com.stockmanager.repository.UserRepository;
import com.stockmanager.service.AuthService;
import com.stockmanager.service.ProductService;
import com.stockmanager.service.UserService;
import com.stockmanager.ui.ConsoleMenu;
import com.stockmanager.ui.InputReader;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        ProductRepository productRepository = new ProductRepository();

        AuthService authService = new AuthService(userRepository);
        ProductService productService = new ProductService(productRepository, authService);
        UserService userService = new UserService(userRepository, authService);

        InputReader inputReader = new InputReader();

        ConsoleMenu consoleMenu = new ConsoleMenu(
                authService,
                productService,
                userService,
                inputReader
        );

        consoleMenu.start();
    }
}