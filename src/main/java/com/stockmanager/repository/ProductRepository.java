package com.stockmanager.repository;

import com.stockmanager.model.Product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private List<Product> products = new ArrayList<>();
    private int nextId = 1;
    private final Path filePath;

    public ProductRepository() {
        this(Path.of("data/products.csv"));
    }

    public ProductRepository(Path filePath) {
        this.filePath = filePath;
        loadFromFile();

        if (products.isEmpty()) {
            seedDefaultProducts();
            saveToFile();
        }
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
        saveToFile();

        return product;
    }

    public void update(int id, String name, double price, int stockQuantity) {
        Product product = findById(id);

        if (product == null) {
            return;
        }

        product.update(name, price, stockQuantity);
        saveToFile();
    }

    public boolean delete(int id) {
        Product product = findById(id);

        if (product == null) {
            return false;
        }

        products.remove(product);
        saveToFile();

        return true;
    }

    private void seedDefaultProducts() {
        products.add(new Product(nextId++, "Clavier", 49.99, 10));
        products.add(new Product(nextId++, "Souris", 19.99, 25));
        products.add(new Product(nextId++, "Écran", 149.99, 5));
    }

    private void loadFromFile() {
        try {
            if (!Files.exists(filePath)) {
                return;
            }

            List<String> lines = Files.readAllLines(filePath);

            for (String line : lines) {
                if (line.isBlank()) {
                    continue;
                }

                String[] parts = line.split(";", -1);

                if (parts.length != 4) {
                    continue;
                }

                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                double price = Double.parseDouble(parts[2]);
                int stockQuantity = Integer.parseInt(parts[3]);

                products.add(new Product(id, name, price, stockQuantity));

                if (id >= nextId) {
                    nextId = id + 1;
                }
            }
        } catch (IOException | NumberFormatException exception) {
            System.out.println("Erreur lors du chargement des produits : " + exception.getMessage());
        }
    }

    private void saveToFile() {
        try {
            Files.createDirectories(filePath.getParent());

            List<String> lines = new ArrayList<>();

            for (Product product : products) {
                String line = product.getId()
                        + ";" + product.getName()
                        + ";" + product.getPrice()
                        + ";" + product.getStockQuantity();

                lines.add(line);
            }

            Files.write(filePath, lines);
        } catch (IOException exception) {
            System.out.println("Erreur lors de la sauvegarde des produits : " + exception.getMessage());
        }
    }
}