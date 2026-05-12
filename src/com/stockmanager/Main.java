package com.stockmanager;

import com.stockmanager.model.Product;
import com.stockmanager.model.Role;
import com.stockmanager.model.User;
import com.stockmanager.repository.ProductRepository;
import com.stockmanager.repository.UserRepository;
import com.stockmanager.service.AuthService;
import com.stockmanager.service.ProductService;
import com.stockmanager.service.UserService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        ProductRepository productRepository = new ProductRepository();

        AuthService authService = new AuthService(userRepository);
        ProductService productService = new ProductService(productRepository);
        UserService userService = new UserService(userRepository);

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

        for (Product product : productService.getAllProducts()) {
            System.out.println(product);
        }

        System.out.println();
        System.out.println("Test création produit via ProductService...");

        Product newProduct = productService.createProduct("Casque", 79.99, 7);

        if (newProduct == null) {
            System.out.println("Erreur lors de la création du produit.");
        } else {
            System.out.println("Produit créé : " + newProduct);
        }

        System.out.println();
        System.out.println("Liste des produits après création :");

        for (Product product : productService.getAllProducts()) {
            System.out.println(product);
        }

        if (currentUser.isAdmin()) {
            System.out.println();
            System.out.println("Test création utilisateur via UserService...");

            User newUser = userService.createUser("test@test.com", "test123", Role.USER);

            if (newUser == null) {
                System.out.println("Erreur lors de la création de l'utilisateur.");
            } else {
                System.out.println("Utilisateur créé : " + newUser);
            }

            System.out.println();
            System.out.println("Liste des utilisateurs :");

            for (User user : userService.getAllUsers()) {
                System.out.println(user);
            }
        }

        scanner.close();
    }
}