package com.stockmanager.ui;

import java.util.List;

import com.stockmanager.model.Product;
import com.stockmanager.model.Role;
import com.stockmanager.model.User;
import com.stockmanager.service.AuthService;
import com.stockmanager.service.ProductService;
import com.stockmanager.service.UserService;

public class ConsoleMenu {
    private AuthService authService;
    private ProductService productService;
    private UserService userService;
    private InputReader inputReader;
    private boolean running = true;

    public ConsoleMenu(
            AuthService authService,
            ProductService productService,
            UserService userService,
            InputReader inputReader
    ) {
        this.authService = authService;
        this.productService = productService;
        this.userService = userService;
        this.inputReader = inputReader;
    }

    public void start() {
        System.out.println("=== Stock Manager Console ===");

        while (running) {
            if (!authService.isLoggedIn()) {
                showLogin();
            } else {
                User currentUser = authService.getCurrentUser();

                if (currentUser.isAdmin()) {
                    showAdminMenu();
                } else {
                    showUserMenu();
                }
            }
        }

        inputReader.close();
        System.out.println("Application terminée.");
    }

    private void showLogin() {
        System.out.println();
        System.out.println("=== Connexion ===");

        String email = inputReader.readString("Email : ");
        String password = inputReader.readString("Mot de passe : ");

        boolean success = authService.login(email, password);

        if (!success) {
            System.out.println("Identifiants incorrects.");
            return;
        }

        User currentUser = authService.getCurrentUser();

        System.out.println("Connexion réussie.");
        System.out.println("Bienvenue " + currentUser.getEmail() + " (" + currentUser.getRole() + ")");
    }

    private void showUserMenu() {
        System.out.println();
        System.out.println("=== Menu USER ===");
        System.out.println("1. Voir tous les produits");
        System.out.println("2. Voir le détail d'un produit");
        System.out.println("3. Se déconnecter");
        System.out.println("4. Quitter l'application");

        int choice = inputReader.readInt("Choix : ");

        switch (choice) {
            case 1:
                showAllProducts();
                break;
            case 2:
                showProductDetails();
                break;
            case 3:
                logout();
                break;
            case 4:
                quit();
                break;
            default:
                System.out.println("Choix invalide.");
        }
    }

    private void showAdminMenu() {
        System.out.println();
        System.out.println("=== Menu ADMIN ===");
        System.out.println("1. Voir tous les produits");
        System.out.println("2. Voir le détail d'un produit");
        System.out.println("3. Ajouter un produit");
        System.out.println("4. Modifier un produit");
        System.out.println("5. Supprimer un produit");
        System.out.println("6. Voir les utilisateurs");
        System.out.println("7. Créer un utilisateur");
        System.out.println("8. Se déconnecter");
        System.out.println("9. Quitter l'application");

        int choice = inputReader.readInt("Choix : ");

        switch (choice) {
            case 1:
                showAllProducts();
                break;
            case 2:
                showProductDetails();
                break;
            case 3:
                createProduct();
                break;
            case 4:
                updateProduct();
                break;
            case 5:
                deleteProduct();
                break;
            case 6:
                showAllUsers();
                break;
            case 7:
                createUser();
                break;
            case 8:
                logout();
                break;
            case 9:
                quit();
                break;
            default:
                System.out.println("Choix invalide.");
        }
    }

    private void showAllProducts() {
        System.out.println();
        System.out.println("=== Liste des produits ===");

        List<Product> products = productService.getAllProducts();

        if (products.isEmpty()) {
            System.out.println("Aucun produit disponible.");
            return;
        }

        for (Product product : products) {
            System.out.println(product);
        }
    }

    private void showProductDetails() {
        System.out.println();
        System.out.println("=== Détail produit ===");

        int id = inputReader.readInt("ID du produit : ");

        try {
            Product product = productService.getProductById(id);

            System.out.println("ID : " + product.getId());
            System.out.println("Nom : " + product.getName());
            System.out.println("Prix : " + product.getPrice() + "€");
            System.out.println("Stock : " + product.getStockQuantity());
            System.out.println("Disponible : " + (product.isInStock() ? "Oui" : "Non"));
        } catch (RuntimeException exception) {
            System.out.println("Erreur : " + exception.getMessage());
        }
    }

    private void createProduct() {
        System.out.println();
        System.out.println("=== Ajouter un produit ===");

        String name = inputReader.readString("Nom : ");
        double price = inputReader.readDouble("Prix : ");
        int stockQuantity = inputReader.readInt("Quantité en stock : ");

        try {
            Product product = productService.createProduct(name, price, stockQuantity);
            System.out.println("Produit créé : " + product);
        } catch (RuntimeException exception) {
            System.out.println("Erreur : " + exception.getMessage());
        }
    }

    private void updateProduct() {
        System.out.println();
        System.out.println("=== Modifier un produit ===");

        int id = inputReader.readInt("ID du produit à modifier : ");

        try {
            Product existingProduct = productService.getProductById(id);

            System.out.println("Produit actuel : " + existingProduct);

            String name = inputReader.readString("Nouveau nom : ");
            double price = inputReader.readDouble("Nouveau prix : ");
            int stockQuantity = inputReader.readInt("Nouvelle quantité en stock : ");

            productService.updateProduct(id, name, price, stockQuantity);

            System.out.println("Produit modifié.");
        } catch (RuntimeException exception) {
            System.out.println("Erreur : " + exception.getMessage());
        }
    }

    private void deleteProduct() {
        System.out.println();
        System.out.println("=== Supprimer un produit ===");

        int id = inputReader.readInt("ID du produit à supprimer : ");

        try {
            Product existingProduct = productService.getProductById(id);

            System.out.println("Produit à supprimer : " + existingProduct);

            String confirmation = inputReader.readString("Confirmer la suppression ? oui/non : ");

            if (!confirmation.equalsIgnoreCase("oui")) {
                System.out.println("Suppression annulée.");
                return;
            }

            productService.deleteProduct(id);

            System.out.println("Produit supprimé.");
        } catch (RuntimeException exception) {
            System.out.println("Erreur : " + exception.getMessage());
        }
    }

    private void showAllUsers() {
        System.out.println();
        System.out.println("=== Liste des utilisateurs ===");

        try {
            List<User> users = userService.getAllUsers();

            if (users.isEmpty()) {
                System.out.println("Aucun utilisateur.");
                return;
            }

            for (User user : users) {
                System.out.println(user);
            }
        } catch (RuntimeException exception) {
            System.out.println("Erreur : " + exception.getMessage());
        }
    }

    private void createUser() {
        System.out.println();
        System.out.println("=== Créer un utilisateur ===");

        String email = inputReader.readString("Email : ");
        String password = inputReader.readString("Mot de passe : ");
        Role role = readRole();

        try {
            User user = userService.createUser(email, password, role);
            System.out.println("Utilisateur créé : " + user);
        } catch (RuntimeException exception) {
            System.out.println("Erreur : " + exception.getMessage());
        }
    }

    private Role readRole() {
        while (true) {
            System.out.println("Rôle :");
            System.out.println("1. ADMIN");
            System.out.println("2. USER");

            int choice = inputReader.readInt("Choix : ");

            switch (choice) {
                case 1:
                    return Role.ADMIN;
                case 2:
                    return Role.USER;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    private void logout() {
        authService.logout();
        System.out.println("Déconnexion réussie.");
    }

    private void quit() {
        running = false;
    }
}