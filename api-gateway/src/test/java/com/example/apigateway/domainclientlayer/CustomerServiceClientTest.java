package com.example.apigateway.domainclientlayer;

import com.example.apigateway.domainclientlayer.customers.CustomerServiceClient;
import com.example.apigateway.presentationlayer.customers.CustomerResponseModel;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceClientTest {

    private CustomerServiceClient customerServiceClient;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        customerServiceClient = new CustomerServiceClient(restTemplate, objectMapper, "host", "port");
    }

    @Test
    public void testGetCustomerByCustomerId() {
        // Arrange
        String customerId = "456";
        String url = "http://host:port/api/v1/customers/" + customerId;
        CustomerResponseModel expectedModel = new CustomerResponseModel("", "", "", "", "", "", "", "");

        // Mocking successful retrieval
        when(restTemplate.getForObject(url, CustomerResponseModel.class)).thenReturn(expectedModel);

        // Act
        CustomerResponseModel result = customerServiceClient.getCustomerByCustomerId(customerId);

        // Assert
        assertNotNull(result);
        verify(restTemplate, times(1)).getForObject(url, CustomerResponseModel.class);
    }

    @Test
    public void testGetCustomerByCustomerId_NotFound() throws IOException {
        // Arrange
        String customerId = "456";
        String url = "http://host:port/api/v1/customers/" + customerId;
        String errorMessage = "Customer not found";

        // Mocking HttpClientErrorException with NOT_FOUND status
        HttpClientErrorException ex = new HttpClientErrorException(HttpStatus.NOT_FOUND, errorMessage);
        when(restTemplate.getForObject(url, CustomerResponseModel.class)).thenThrow(ex);

        // Act & Assert
        Exception exception = assertThrows(NotFoundException.class, () -> {
            customerServiceClient.getCustomerByCustomerId(customerId);
        });
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        verify(restTemplate, times(1)).getForObject(url, CustomerResponseModel.class);
    }

    @Test
    public void testGetCustomerByCustomerId_UnprocessableEntity() throws IOException {
        // Arrange
        String customerId = "456";
        String url = "http://host:port/api/v1/customers/" + customerId;
        String errorMessage = "Unprocessable entity";

        // Mocking HttpClientErrorException with UNPROCESSABLE_ENTITY status
        HttpClientErrorException ex = new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY, errorMessage);
        when(restTemplate.getForObject(url, CustomerResponseModel.class)).thenThrow(ex);

        // Act & Assert
        Exception exception = assertThrows(InvalidInputException.class, () -> {
            customerServiceClient.getCustomerByCustomerId(customerId);
        });
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        verify(restTemplate, times(1)).getForObject(url, CustomerResponseModel.class);
    }

    @Test
    public void testGetCustomerByCustomer_BadRequest() {
        // Arrange
        String customerId = "123";
        String url = "http://host:port/api/v1/customers/" + customerId;
        String errorMessage = "Bad Request";

        HttpClientErrorException ex = new HttpClientErrorException(HttpStatus.BAD_REQUEST, errorMessage);
        when(restTemplate.getForObject(url, CustomerResponseModel.class)).thenThrow(ex);

        // Act & Assert
        Exception exception = assertThrows(InvalidInputException.class, () -> {
            customerServiceClient.getCustomerByCustomerId(customerId);
        });
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        verify(restTemplate, times(1)).getForObject(url, CustomerResponseModel.class);
    }

    @Test
    public void testGetRingByRingId_NoValidError() {
        // Arrange
        String customerId = "123";
        String url = "http://host:port/api/v1/customers/" + customerId;
        String errorMessage = "No valid error";

        HttpClientErrorException ex = new HttpClientErrorException(HttpStatus.CREATED, errorMessage);
        when(restTemplate.getForObject(url, CustomerResponseModel.class)).thenThrow(ex);

        // Act & Assert
        Exception exception = assertThrows(HttpClientErrorException.class, () -> {
            customerServiceClient.getCustomerByCustomerId(customerId);
        });
        assertNotNull(exception);
        assertEquals("201 " +errorMessage, exception.getMessage());
        verify(restTemplate, times(1)).getForObject(url, CustomerResponseModel.class);
    }

}
