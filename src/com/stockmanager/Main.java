package com.stockmanager;

import com.stockmanager.model.Product;
import com.stockmanager.model.Role;
import com.stockmanager.model.User;
import com.stockmanager.repository.ProductRepository;
import com.stockmanager.repository.UserRepository;
import com.stockmanager.service.AuthService;
import com.stockmanager.service.ProductService;
import com.stockmanager.service.UserService;
import com.stockmanager.ui.InputReader;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        ProductRepository productRepository = new ProductRepository();

        AuthService authService = new AuthService(userRepository);
        ProductService productService = new ProductService(productRepository);
        UserService userService = new UserService(userRepository);

        InputReader inputReader = new InputReader();

        System.out.println("=== Stock Manager Console ===");
        System.out.println("=== Login ===");

        String email = inputReader.readString("Email: ");
        String password = inputReader.readString("Password: ");

        boolean success = authService.login(email, password);

        if (!success) {
            System.out.println("Identifiants incorrects.");
            inputReader.close();
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
        System.out.println("Test création produit avec InputReader");

        String productName = inputReader.readString("Nom du produit : ");
        double productPrice = inputReader.readDouble("Prix du produit : ");
        int productStock = inputReader.readInt("Quantité en stock : ");

        Product newProduct = productService.createProduct(productName, productPrice, productStock);

        if (newProduct == null) {
            System.out.println("Erreur lors de la création du produit.");
        } else {
            System.out.println("Produit créé : " + newProduct);
        }

        if (currentUser.isAdmin()) {
            System.out.println();
            System.out.println("Test création utilisateur admin");

            String newUserEmail = inputReader.readString("Email du nouvel utilisateur : ");
            String newUserPassword = inputReader.readString("Mot de passe du nouvel utilisateur : ");

            User newUser = userService.createUser(newUserEmail, newUserPassword, Role.USER);

            if (newUser == null) {
                System.out.println("Erreur lors de la création de l'utilisateur.");
            } else {
                System.out.println("Utilisateur créé : " + newUser);
            }
        }

        inputReader.close();
    }
}