package com.example.apigateway.domainclientlayer;

import com.example.apigateway.domainclientlayer.products.ProductServiceClient;
import com.example.apigateway.presentationlayer.products.ProductResponseModel;
import com.example.apigateway.utils.exceptions.InvalidInputException;
import com.example.apigateway.utils.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceClientTest {

    private ProductServiceClient productServiceClient;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        productServiceClient = new ProductServiceClient(restTemplate, objectMapper, "host", "port");
    }

    @Test
    public void testGetProductByProductId() {
        // Arrange
        String productId = "456";
        String url = "http://host:port/api/v1/products/" + productId;
        ProductResponseModel expectedModel = new ProductResponseModel("", "", "", "", new BigDecimal("0"), 0, "");

        // Mocking successful retrieval
        when(restTemplate.getForObject(url, ProductResponseModel.class)).thenReturn(expectedModel);

        // Act
        ProductResponseModel result = productServiceClient.getProductByProductId(productId);

        // Assert
        assertNotNull(result);
        verify(restTemplate, times(1)).getForObject(url, ProductResponseModel.class);
    }

    @Test
    public void testGetProductByProductId_NotFound() throws IOException {
        // Arrange
        String productId = "456";
        String url = "http://host:port/api/v1/products/" + productId;
        String errorMessage = "Product not found";

        // Mocking HttpClientErrorException with NOT_FOUND status
        HttpClientErrorException ex = new HttpClientErrorException(HttpStatus.NOT_FOUND, errorMessage);
        when(restTemplate.getForObject(url, ProductResponseModel.class)).thenThrow(ex);

        // Act & Assert
        Exception exception = assertThrows(NotFoundException.class, () -> {
            productServiceClient.getProductByProductId(productId);
        });
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        verify(restTemplate, times(1)).getForObject(url, ProductResponseModel.class);
    }

    @Test
    public void testGetProductByProductId_UnprocessableEntity() throws IOException {
        // Arrange
        String productId = "456";
        String url = "http://host:port/api/v1/products/" + productId;
        String errorMessage = "Unprocessable entity";

        // Mocking HttpClientErrorException with UNPROCESSABLE_ENTITY status
        HttpClientErrorException ex = new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY, errorMessage);
        when(restTemplate.getForObject(url, ProductResponseModel.class)).thenThrow(ex);

        // Act & Assert
        Exception exception = assertThrows(InvalidInputException.class, () -> {
            productServiceClient.getProductByProductId(productId);
        });
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        verify(restTemplate, times(1)).getForObject(url, ProductResponseModel.class);
    }

    @Test
    public void testGetProductByProductId_BadRequest() {
        // Arrange
        String productId = "123";
        String url = "http://host:port/api/v1/products/" + productId;
        String errorMessage = "Bad Request";

        HttpClientErrorException ex = new HttpClientErrorException(HttpStatus.BAD_REQUEST, errorMessage);
        when(restTemplate.getForObject(url, ProductResponseModel.class)).thenThrow(ex);

        // Act & Assert
        Exception exception = assertThrows(InvalidInputException.class, () -> {
            productServiceClient.getProductByProductId(productId);
        });
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        verify(restTemplate, times(1)).getForObject(url, ProductResponseModel.class);
    }

    @Test
    public void testGetProductByProductId_NoValidError() {
        // Arrange
        String productId = "123";
        String url = "http://host:port/api/v1/products/" + productId;
        String errorMessage = "No valid error";

        HttpClientErrorException ex = new HttpClientErrorException(HttpStatus.CREATED, errorMessage);
        when(restTemplate.getForObject(url, ProductResponseModel.class)).thenThrow(ex);

        // Act & Assert
        Exception exception = assertThrows(HttpClientErrorException.class, () -> {
            productServiceClient.getProductByProductId(productId);
        });
        assertNotNull(exception);
        assertEquals("201 " + errorMessage, exception.getMessage());
        verify(restTemplate, times(1)).getForObject(url, ProductResponseModel.class);
    }

}
