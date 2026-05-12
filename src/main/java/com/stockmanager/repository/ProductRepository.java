package com.stockmanager.repository;

import java.util.ArrayList;
import java.util.List;

import com.stockmanager.model.Product;

public class ProductRepository {
    private List<Product> products = new ArrayList<>();
    private int nextId = 1;

    public ProductRepository() {
        create("Clavier", 49.99, 10);
        create("Souris", 19.99, 25);
        create("Écran", 149.99, 5);
    }

    public List<Product> findAll() {
        return products;
    }

    public Product findById(int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }

        return null;
    }

    public Product create(String name, double price, int stockQuantity) {
        Product product = new Product(nextId++, name, price, stockQuantity);
        products.add(product);

        return product;
    }

    public boolean delete(int id) {
        Product product = findById(id);

        if (product == null) {
            return false;
        }

        products.remove(product);
        return true;
    }
}