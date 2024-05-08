package com.example.products.presentationlayer;

import com.example.products.businesslayer.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ProductController.class)
class ProductControllerUnitTest {

    private final String FOUND_PRODUCT_ID = "123e4567-a456-12d3-e89b-556642440000";
    private final String NOT_FOUND_PRODUCT_ID = "nonExistentId";

    @Autowired
    ProductController productController;

    @MockBean
    private ProductServiceImpl productService;

    @Test
    public void whenNoProductExists_thenReturnEmptyList() {
        when(productService.getProducts()).thenReturn(Collections.emptyList());

        ResponseEntity<List<ProductResponseModel>> responseEntity = productController.getProducts();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().isEmpty());

        verify(productService, times(1)).getProducts();
    }

    @Test
    public void whenProductExists_thenReturnProduct() {
        ProductRequestModel productRequestModel = buildProductRequestModel();
        ProductResponseModel productResponseModel = buildProductResponseModel();

        when(productService.addProduct(productRequestModel)).thenReturn(productResponseModel);

        ResponseEntity<ProductResponseModel> responseEntity = productController.createProduct(productRequestModel);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(productResponseModel, responseEntity.getBody());

        verify(productService, times(1)).addProduct(productRequestModel);
    }

    private ProductRequestModel buildProductRequestModel() {
        return ProductRequestModel.builder()
                .productId(FOUND_PRODUCT_ID)
                .name("T-Shirt")
                .description("Plain white cotton T-shirt")
                .size("Medium")
                .price(new BigDecimal("15.99"))
                .quantity(50)
                .status("IN_STOCK")
                .build();
    }

    private ProductResponseModel buildProductResponseModel() {
        return ProductResponseModel.builder()
                .productId(FOUND_PRODUCT_ID)
                .name("T-Shirt")
                .description("Plain white cotton T-shirt")
                .size("Medium")
                .price(new BigDecimal("15.99"))
                .quantity(50)
                .status("IN_STOCK")
                .build();
    }
}
