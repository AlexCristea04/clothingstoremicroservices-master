package com.example.products.datalayer;

import com.example.products.datalayer.ProductStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryIntegrationTest {
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void setUpDb() {
        productRepository.deleteAll();
    }

    @Test
    public void whenProductExists_ReturnProductByProductId() {

        // Initialize Product
        Product product = new Product(
                "T-Shirt", // Last Name
                "Plain white cotton T-shirt", // First Name
                "Medium", // Email Address
                new BigDecimal("15.99"),
                50,
                ProductStatus.IN_STOCK
        );

        productRepository.save(product);

        // Act
        Product productFound = productRepository.findByProductIdentifier_ProductId(product.getProductIdentifier().getProductId());

        // Assert
        assertNotNull(productFound);
        assertEquals(product, productFound);
    }
}
