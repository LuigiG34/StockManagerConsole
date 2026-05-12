package com.stockmanager.service;

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
        return productRepository.findById(id);
    }

    public Product createProduct(String name, double price, int stockQuantity) {
        if (!currentUserIsAdmin()) {
            return null;
        }

        if (!isValidProductData(name, price, stockQuantity)) {
            return null;
        }

        return productRepository.create(name, price, stockQuantity);
    }

    public boolean updateProduct(int id, String name, double price, int stockQuantity) {
        if (!currentUserIsAdmin()) {
            return false;
        }

        Product product = productRepository.findById(id);

        if (product == null) {
            return false;
        }

        if (!isValidProductData(name, price, stockQuantity)) {
            return false;
        }

        product.update(name, price, stockQuantity);

        return true;
    }

    public boolean deleteProduct(int id) {
        if (!currentUserIsAdmin()) {
            return false;
        }

        return productRepository.delete(id);
    }

    private boolean isValidProductData(String name, double price, int stockQuantity) {
        if (name == null || name.isBlank()) {
            return false;
        }

        if (price <= 0) {
            return false;
        }

        return stockQuantity >= 0;
    }

    private boolean currentUserIsAdmin() {
        User currentUser = authService.getCurrentUser();

        if (currentUser == null) {
            return false;
        }

        return currentUser.isAdmin();
    }
}