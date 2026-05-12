package com.stockmanager;

import com.stockmanager.model.User;
import com.stockmanager.repository.ProductRepository;
import com.stockmanager.repository.UserRepository;
import com.stockmanager.service.AuthService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        ProductRepository productRepository = new ProductRepository();
        AuthService authService = new AuthService(userRepository);

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Stock Manager Console ===");
        System.out.println("=== Login ===");

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        boolean success = authService.login(email, password);

        if (!success) {
            System.out.println("Identifiants incorrects.");
            scanner.close();
            return;
        }

        User currentUser = authService.getCurrentUser();

        System.out.println("Connexion réussie.");
        System.out.println("Bienvenue " + currentUser.getEmail());
        System.out.println("Rôle : " + currentUser.getRole());

        System.out.println();
        System.out.println("Produits disponibles :");

        productRepository.findAll().forEach(System.out::println);

        scanner.close();
    }
}