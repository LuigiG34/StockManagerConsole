package com.stockmanager.service;

import com.stockmanager.exception.NotFoundException;
import com.stockmanager.exception.UnauthorizedException;
import com.stockmanager.exception.ValidationException;
import com.stockmanager.model.Product;
import com.stockmanager.model.User;
import com.stockmanager.repository.ProductRepository;

import java.util.List;

public class ProductService {
    private ProductRepository productRepository;
    private AuthService authService;

    public ProductService(ProductRepository productRepository, AuthService authService) {
        this.productRepository = productRepository;
        this.authService = authService;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(int id) {
        Product product = productRepository.findById(id);

        if (product == null) {
            throw new NotFoundException("Produit introuvable.");
        }

        return product;
    }

    public Product createProduct(String name, double price, int stockQuantity) {
        requireAdmin();
        validateProductData(name, price, stockQuantity);

        return productRepository.create(name, price, stockQuantity);
    }

    public void updateProduct(int id, String name, double price, int stockQuantity) {
        requireAdmin();

        Product product = productRepository.findById(id);

        if (product == null) {
            throw new NotFoundException("Produit introuvable.");
        }

        validateProductData(name, price, stockQuantity);

        productRepository.update(id, name, price, stockQuantity);
    }

    public void deleteProduct(int id) {
        requireAdmin();

        boolean deleted = productRepository.delete(id);

        if (!deleted) {
            throw new NotFoundException("Produit introuvable.");
        }
    }

    private void validateProductData(String name, double price, int stockQuantity) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Le nom du produit ne peut pas être vide.");
        }

        if (price <= 0) {
            throw new ValidationException("Le prix doit être supérieur à 0.");
        }

        if (stockQuantity < 0) {
            throw new ValidationException("Le stock ne peut pas être négatif.");
        }
    }

    private void requireAdmin() {
        User currentUser = authService.getCurrentUser();

        if (currentUser == null || !currentUser.isAdmin()) {
            throw new UnauthorizedException("Action réservée aux administrateurs.");
        }
    }
}