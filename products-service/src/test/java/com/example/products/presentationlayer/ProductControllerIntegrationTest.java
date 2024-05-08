package com.example.products.presentationlayer;

import com.example.products.datalayer.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("h2")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductControllerIntegrationTest {

    private final String BASE_URI_PRODUCTS = "/api/v1/products";
    private final String FOUND_PRODUCT_ID = "123e4567-a456-12d3-e89b-556642440000";
    private final String NOT_FOUND_PRODUCT_ID = "c3333333-3333-3333-444444444444";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void whenGetProducts_thenReturnAllProducts() {
        long sizeDB = productRepository.count();

        webTestClient.get().uri(BASE_URI_PRODUCTS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(ProductResponseModel.class)
                .value((list) -> {
                    assertNotNull(list);
                    assertEquals(list.size(), sizeDB);
                });
    }

    @Test
    public void whenGetProductDoesNotExist_thenReturnNotFound() {
        webTestClient.get().uri(BASE_URI_PRODUCTS + "/" + NOT_FOUND_PRODUCT_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown productId: " + NOT_FOUND_PRODUCT_ID);
    }

    @Test
    public void whenValidProduct_thenCreateProduct() {
        long sizeDB = productRepository.count();

        ProductRequestModel productRequestModel = ProductRequestModel.builder()
                .productId(FOUND_PRODUCT_ID)
                .name("T-Shirt")
                .description("Plain white cotton T-shirt")
                .size("Medium")
                .price(new BigDecimal("15.99"))
                .quantity(50)
                .status("IN_STOCK")
                .build();

        webTestClient.post()
                .uri(BASE_URI_PRODUCTS)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.valueOf("application/hal+json"))
                .expectBody(ProductResponseModel.class)
                .value((productResponseModel) -> {
                    assertNotNull(productResponseModel);
                    assertEquals(productRequestModel.getName(), productResponseModel.getName());
                    assertEquals(productRequestModel.getDescription(), productResponseModel.getDescription());
                    assertEquals(productRequestModel.getSize(), productResponseModel.getSize());
                    assertEquals(productRequestModel.getPrice(), productResponseModel.getPrice());
                    assertEquals(productRequestModel.getQuantity(), productResponseModel.getQuantity());
                    assertEquals(productRequestModel.getStatus(), productResponseModel.getStatus());
                });

        long sizeDBAfter = productRepository.count();
        assertEquals(sizeDB + 1, sizeDBAfter);
    }

    @Test
    public void whenUpdateProduct_thenReturnUpdatedProduct() {
        ProductRequestModel productToUpdate = ProductRequestModel.builder()
                .productId(FOUND_PRODUCT_ID)
                .name("T-Shirt")
                .description("Plain white cotton T-shirt")
                .size("Medium")
                .price(new BigDecimal("15.99"))
                .quantity(30)
                .status("IN_STOCK")
                .build();

        webTestClient.put()
                .uri(BASE_URI_PRODUCTS + "/" + FOUND_PRODUCT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productToUpdate)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.valueOf("application/hal+json"))
                .expectBody(ProductResponseModel.class)
                .value((updatedProduct) -> {
                    assertNotNull(updatedProduct);
                    assertEquals(productToUpdate.getProductId(), updatedProduct.getProductId());
                    assertEquals(productToUpdate.getName(), updatedProduct.getName());
                    assertEquals(productToUpdate.getDescription(), updatedProduct.getDescription());
                    assertEquals(productToUpdate.getSize(), updatedProduct.getSize());
                    assertEquals(productToUpdate.getPrice(), updatedProduct.getPrice());
                    assertEquals(productToUpdate.getQuantity(), updatedProduct.getQuantity());
                    assertEquals(productToUpdate.getStatus(), updatedProduct.getStatus());
                });
    }

    @Test
    public void whenUpdateNonExistentProduct_thenThrowNotFoundException() {
        String nonExistentProductId = "nonExistentId";
        ProductRequestModel updatedProduct = ProductRequestModel.builder()
                .productId(nonExistentProductId)
                .name("Update Name")
                .description("Update Description")
                .size("Medium")
                .price(new BigDecimal("15.99"))
                .quantity(20)
                .status("IN_STOCK")
                .build();

        webTestClient.put()
                .uri(BASE_URI_PRODUCTS + "/" + nonExistentProductId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedProduct)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown productId: " + nonExistentProductId);
    }

    @Test
    public void whenRemoveNonExistentProduct_thenThrowNotFoundException() {
        String nonExistentProductId = "nonExistentId";

        webTestClient.delete().uri(BASE_URI_PRODUCTS + "/" + nonExistentProductId)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown productId: " + nonExistentProductId);
    }

    @Test
    public void whenDeleteProduct_thenDeleteProductSuccessfully() {
        webTestClient.delete().uri(BASE_URI_PRODUCTS + "/" + FOUND_PRODUCT_ID)
                .exchange()
                .expectStatus()
                .isNoContent();

        assertFalse(productRepository.existsProductByProductIdentifier_ProductId(FOUND_PRODUCT_ID));
    }

    @Test
    public void whenGetProductById_thenReturnProduct() {
        webTestClient.get().uri(BASE_URI_PRODUCTS + "/" + FOUND_PRODUCT_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductResponseModel.class)
                .value((product) -> {
                    assertNotNull(product);
                    assertEquals(FOUND_PRODUCT_ID, product.getProductId());
                    assertEquals("T-Shirt", product.getName());
                    assertEquals("Plain white cotton T-shirt", product.getDescription());
                    assertEquals("Medium", product.getSize());
                    assertEquals(new BigDecimal("15.99"), product.getPrice());
                    assertEquals(50, product.getQuantity());
                    assertEquals("IN_STOCK", product.getStatus());
                });
    }
}
