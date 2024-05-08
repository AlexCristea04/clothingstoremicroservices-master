package com.example.apigateway.presentationlayer.products;

import com.example.apigateway.businesslayer.products.ProductServiceImpl;
import com.example.apigateway.domainclientlayer.products.ProductServiceClient;
import com.example.apigateway.mapperlayer.products.ProductResponseMapper;
import com.example.apigateway.mapperlayer.products.ProductResponseMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ProductControllerIntegrationTest {
    private ProductServiceClient mockClient;
    private ProductResponseMapper mockMapper;
    private ProductServiceImpl productService;
    private ProductResponseMapper productResponseMapper;

    @BeforeEach
    void setUp() {
        mockClient = mock(ProductServiceClient.class);
        mockMapper = mock(ProductResponseMapper.class);
        productResponseMapper = new ProductResponseMapperImpl();
        productService = new ProductServiceImpl(mockClient, mockMapper);
    }

    @Test
    void testGetAllProducts() {
        // Arrange
        List<ProductResponseModel> products = Collections.singletonList(new ProductResponseModel());
        List<ProductResponseModel> expectedResponseModels = Collections.singletonList(new ProductResponseModel());
        when(mockClient.getAllProducts()).thenReturn(products);
        when(mockMapper.responseModelListToResponseModelList(products)).thenReturn(expectedResponseModels);

        // Act
        List<ProductResponseModel> result = productService.getProducts();

        // Assert
        assertEquals(expectedResponseModels.size(), result.size());
    }

    @Test
    void testGetProductByProductId() {
        // Arrange
        String productId = "123";

        ProductResponseModel product = new ProductResponseModel();
        ProductResponseModel expectedResponseModel = new ProductResponseModel();

        when(mockClient.getProductByProductId(productId)).thenReturn(product);
        when(mockMapper.responseModelToResponseModel(product)).thenReturn(expectedResponseModel);

        // Act
        ProductResponseModel result = productService.getProductByProductId(productId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponseModel, result);
    }

    @Test
    void testCreateProduct() {
        // Arrange
        ProductRequestModel requestModel = new ProductRequestModel();
        ProductResponseModel product = new ProductResponseModel();
        ProductResponseModel expectedResponseModel = new ProductResponseModel();

        when(mockClient.createProduct(requestModel)).thenReturn(product);
        when(mockMapper.responseModelToResponseModel(product)).thenReturn(expectedResponseModel);

        // Act
        ProductResponseModel result = productService.addProduct(requestModel);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponseModel, result);
    }

    @Test
    void testUpdateProduct() {
        // Arrange
        String productId = "123";
        ProductRequestModel requestModel = new ProductRequestModel();
        ProductResponseModel product = new ProductResponseModel();
        ProductResponseModel expectedResponseModel = new ProductResponseModel();

        when(mockClient.updateProductByProduct_Id(requestModel, productId)).thenReturn(product);
        when(mockMapper.responseModelToResponseModel(product)).thenReturn(expectedResponseModel);

        // Act
        ProductResponseModel result = productService.updateProduct(requestModel, productId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponseModel, result);
    }

    @Test
    void testDeleteProductByProductId() {
        // Arrange
        String productId = "123";

        // Act
        productService.removeProduct(productId);

        // Assert
        verify(mockClient, times(1)).deleteProductByProduct_Id(productId);
    }

    @Test
    void testMapRequestToResponse() {
        // Arrange
        ProductResponseModel responseModel = new ProductResponseModel();
        responseModel.setProductId("1");
        responseModel.setName("ProductName");

        // Act
        ProductResponseModel responseModel2 = productResponseMapper.responseModelToResponseModel(responseModel);

        // Assert
        assertNotNull(responseModel2);
        assertEquals(responseModel2.getProductId(), responseModel.getProductId());
        assertEquals(responseModel2.getName(), responseModel.getName());
    }
}
