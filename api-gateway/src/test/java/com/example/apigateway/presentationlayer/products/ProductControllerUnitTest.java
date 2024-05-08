package com.example.apigateway.presentationlayer.products;

import com.example.apigateway.businesslayer.products.ProductService;
import com.example.apigateway.businesslayer.products.ProductServiceImpl;
import com.example.apigateway.domainclientlayer.products.ProductServiceClient;
import com.example.apigateway.mapperlayer.products.ProductResponseMapper;
import com.example.apigateway.mapperlayer.products.ProductResponseMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ProductControllerUnitTest {

    private ProductController productController;
    private ProductResponseMapper mockProductResponseMapper;
    private ProductService mockProductService;

    @BeforeEach
    void setUp() {
        mockProductService = mock(ProductService.class);
        mockProductResponseMapper = mock(ProductResponseMapper.class);
        productController = new ProductController(mockProductService);
    }

    @Test
    void testCreateProduct() {
        // Arrange
        ProductRequestModel requestModel = new ProductRequestModel();
        ProductResponseModel expectedResponse = new ProductResponseModel();
        expectedResponse.setProductId(UUID.randomUUID().toString());
        when(mockProductService.addProduct(requestModel)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ProductResponseModel> result = productController.createProduct(requestModel);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(expectedResponse, result.getBody());
    }

    @Test
    void testUpdateProduct() {
        // Arrange
        String productId = "123";
        ProductRequestModel requestModel = new ProductRequestModel();
        ProductResponseModel expectedResponse = new ProductResponseModel();
        expectedResponse.setProductId(UUID.randomUUID().toString());
        when(mockProductService.updateProduct(eq(requestModel), eq(productId))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ProductResponseModel> result = productController.updateProduct(requestModel, productId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(expectedResponse, result.getBody());
    }

    @Test
    void testDeleteProduct() {
        // Arrange
        String productId = "123";

        // Act
        productController.deleteProduct(productId);

        // Assert
        verify(mockProductService, times(1)).removeProduct(productId);
    }

    @Test
    void testGetProductById() {
        // Arrange
        String productId = "123";
        ProductResponseModel expectedResponse = new ProductResponseModel();
        when(mockProductService.getProductByProductId(productId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ProductResponseModel> responseEntity = productController.getProductByProductId(productId);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void testGetProducts() {
        // Arrange
        List<ProductResponseModel> expectedProducts = Collections.singletonList(new ProductResponseModel());
        when(mockProductService.getProducts()).thenReturn(expectedProducts);

        // Act
        ResponseEntity<List<ProductResponseModel>> responseEntity = productController.getProducts();

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(expectedProducts.size(), responseEntity.getBody().size());

        ProductResponseModel expectedProduct = expectedProducts.get(0);
        ProductResponseModel actualProduct = responseEntity.getBody().get(0);
        assertEquals(expectedProduct.getProductId(), actualProduct.getProductId());
        assertEquals(expectedProduct.getName(), actualProduct.getName());

        verify(mockProductService, times(1)).getProducts();
    }
}
