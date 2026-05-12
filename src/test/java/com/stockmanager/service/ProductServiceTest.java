package com.stockmanager.service;

import com.stockmanager.exception.NotFoundException;
import com.stockmanager.exception.UnauthorizedException;
import com.stockmanager.exception.ValidationException;
import com.stockmanager.model.Product;
import com.stockmanager.repository.ProductRepository;
import com.stockmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {
    @TempDir
    Path tempDir;

    @Test
    void shouldListDefaultProducts() {
        UserRepository userRepository = new UserRepository(tempDir.resolve("users.csv"));
        ProductRepository productRepository = new ProductRepository(tempDir.resolve("products.csv"));
        AuthService authService = new AuthService(userRepository);

        ProductService productService = new ProductService(productRepository, authService);

        List<Product> products = productService.getAllProducts();

        assertEquals(3, products.size());
        assertEquals("Clavier", products.get(0).getName());
    }

    @Test
    void shouldGetProductById() {
        UserRepository userRepository = new UserRepository(tempDir.resolve("users.csv"));
        ProductRepository productRepository = new ProductRepository(tempDir.resolve("products.csv"));
        AuthService authService = new AuthService(userRepository);

        ProductService productService = new ProductService(productRepository, authService);

        Product product = productService.getProductById(1);

        assertEquals(1, product.getId());
        assertEquals("Clavier", product.getName());
    }

    @Test
    void shouldThrowExceptionWhenProductDoesNotExist() {
        UserRepository userRepository = new UserRepository(tempDir.resolve("users.csv"));
        ProductRepository productRepository = new ProductRepository(tempDir.resolve("products.csv"));
        AuthService authService = new AuthService(userRepository);

        ProductService productService = new ProductService(productRepository, authService);

        assertThrows(
                NotFoundException.class,
                () -> productService.getProductById(999)
        );
    }

    @Test
    void adminShouldCreateProduct() {
        UserRepository userRepository = new UserRepository(tempDir.resolve("users.csv"));
        ProductRepository productRepository = new ProductRepository(tempDir.resolve("products.csv"));

        AuthService authService = new AuthService(userRepository);
        authService.login("admin@test.com", "admin123");

        ProductService productService = new ProductService(productRepository, authService);

        Product product = productService.createProduct("Casque", 79.99, 5);

        assertNotNull(product);
        assertEquals("Casque", product.getName());
        assertEquals(79.99, product.getPrice());
        assertEquals(5, product.getStockQuantity());
        assertEquals(4, productService.getAllProducts().size());
    }

    @Test
    void userShouldNotCreateProduct() {
        UserRepository userRepository = new UserRepository(tempDir.resolve("users.csv"));
        ProductRepository productRepository = new ProductRepository(tempDir.resolve("products.csv"));

        AuthService authService = new AuthService(userRepository);
        authService.login("user@test.com", "user123");

        ProductService productService = new ProductService(productRepository, authService);

        assertThrows(
                UnauthorizedException.class,
                () -> productService.createProduct("Casque", 79.99, 5)
        );
    }

    @Test
    void productNameShouldNotBeBlank() {
        UserRepository userRepository = new UserRepository(tempDir.resolve("users.csv"));
        ProductRepository productRepository = new ProductRepository(tempDir.resolve("products.csv"));

        AuthService authService = new AuthService(userRepository);
        authService.login("admin@test.com", "admin123");

        ProductService productService = new ProductService(productRepository, authService);

        assertThrows(
                ValidationException.class,
                () -> productService.createProduct("", 79.99, 5)
        );
    }

    @Test
    void priceShouldBeGreaterThanZero() {
        UserRepository userRepository = new UserRepository(tempDir.resolve("users.csv"));
        ProductRepository productRepository = new ProductRepository(tempDir.resolve("products.csv"));

        AuthService authService = new AuthService(userRepository);
        authService.login("admin@test.com", "admin123");

        ProductService productService = new ProductService(productRepository, authService);

        assertThrows(
                ValidationException.class,
                () -> productService.createProduct("Casque", 0, 5)
        );
    }

    @Test
    void stockShouldNotBeNegative() {
        UserRepository userRepository = new UserRepository(tempDir.resolve("users.csv"));
        ProductRepository productRepository = new ProductRepository(tempDir.resolve("products.csv"));

        AuthService authService = new AuthService(userRepository);
        authService.login("admin@test.com", "admin123");

        ProductService productService = new ProductService(productRepository, authService);

        assertThrows(
                ValidationException.class,
                () -> productService.createProduct("Casque", 79.99, -1)
        );
    }

    @Test
    void adminShouldUpdateProduct() {
        UserRepository userRepository = new UserRepository(tempDir.resolve("users.csv"));
        ProductRepository productRepository = new ProductRepository(tempDir.resolve("products.csv"));

        AuthService authService = new AuthService(userRepository);
        authService.login("admin@test.com", "admin123");

        ProductService productService = new ProductService(productRepository, authService);

        productService.updateProduct(1, "Clavier mécanique", 99.99, 3);

        Product product = productService.getProductById(1);

        assertEquals("Clavier mécanique", product.getName());
        assertEquals(99.99, product.getPrice());
        assertEquals(3, product.getStockQuantity());
    }

    @Test
    void adminShouldDeleteProduct() {
        UserRepository userRepository = new UserRepository(tempDir.resolve("users.csv"));
        ProductRepository productRepository = new ProductRepository(tempDir.resolve("products.csv"));

        AuthService authService = new AuthService(userRepository);
        authService.login("admin@test.com", "admin123");

        ProductService productService = new ProductService(productRepository, authService);

        productService.deleteProduct(1);

        assertEquals(2, productService.getAllProducts().size());

        assertThrows(
                NotFoundException.class,
                () -> productService.getProductById(1)
        );
    }
}