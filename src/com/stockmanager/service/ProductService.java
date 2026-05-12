package com.stockmanager.service;

import com.stockmanager.model.Product;
import com.stockmanager.repository.ProductRepository;

import java.util.List;

public class ProductService {
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(int id) {
        return productRepository.findById(id);
    }

    public Product createProduct(String name, double price, int stockQuantity) {
        if (name == null || name.isBlank()) {
            return null;
        }

        if (price <= 0) {
            return null;
        }

        if (stockQuantity < 0) {
            return null;
        }

        return productRepository.create(name, price, stockQuantity);
    }

    public boolean updateProduct(int id, String name, double price, int stockQuantity) {
        Product product = productRepository.findById(id);

        if (product == null) {
            return false;
        }

        if (name == null || name.isBlank()) {
            return false;
        }

        if (price <= 0) {
            return false;
        }

        if (stockQuantity < 0) {
            return false;
        }

        product.update(name, price, stockQuantity);

        return true;
    }

    public boolean deleteProduct(int id) {
        return productRepository.delete(id);
    }
}