package com.example.apigateway.presentationlayer.customers;

import com.example.apigateway.businesslayer.customers.CustomerService;
import com.example.apigateway.businesslayer.customers.CustomerServiceImpl;
import com.example.apigateway.domainclientlayer.customers.CustomerServiceClient;
import com.example.apigateway.mapperlayer.customers.CustomerResponseMapper;
import com.example.apigateway.mapperlayer.customers.CustomerResponseMapperImpl;
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

public class CustomerControllerUnitTest {

    private CustomerController customerController;
    private CustomerResponseMapper mockCustomerResponseMapper;
    private CustomerService mockCustomerService;

    @BeforeEach
    void setUp() {
        mockCustomerService = mock(CustomerService.class);
        mockCustomerResponseMapper = mock(CustomerResponseMapper.class);
        customerController = new CustomerController(mockCustomerService);
    }

    @Test
    void testCreateCustomer() {
        // Arrange
        CustomerRequestModel requestModel = new CustomerRequestModel();
        CustomerResponseModel expectedResponse = new CustomerResponseModel();
        expectedResponse.setCustomerId(UUID.randomUUID().toString());
        when(mockCustomerService.addCustomer(requestModel)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<CustomerResponseModel> result = customerController.createCustomer(requestModel);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(expectedResponse, result.getBody());
    }

    @Test
    void testUpdateCustomer() {
        // Arrange
        String customerId = "123";
        CustomerRequestModel requestModel = new CustomerRequestModel();
        CustomerResponseModel expectedResponse = new CustomerResponseModel();
        expectedResponse.setCustomerId(UUID.randomUUID().toString());
        when(mockCustomerService.updateCustomer(eq(requestModel), eq(customerId))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<CustomerResponseModel> result = customerController.updateCustomer(requestModel, customerId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(expectedResponse, result.getBody());
    }

    @Test
    void testDeleteCustomer() {
        // Arrange
        String customerId = "123";

        // Act
        customerController.deleteCustomer(customerId);

        // Assert
        verify(mockCustomerService, times(1)).removeCustomer(customerId);
    }

    @Test
    void testGetCustomerById() {
        // Arrange
        String customerId = "123";
        CustomerResponseModel expectedResponse = new CustomerResponseModel();
        when(mockCustomerService.getCustomerByCustomerId(customerId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<CustomerResponseModel> responseEntity = customerController.getCustomerByCustomerId(customerId);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void testGetCustomers() {
        // Arrange
        List<CustomerResponseModel> expectedCustomers = Collections.singletonList(new CustomerResponseModel());
        when(mockCustomerService.getCustomers()).thenReturn(expectedCustomers);

        // Act
        ResponseEntity<List<CustomerResponseModel>> responseEntity = customerController.getCustomers();

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(expectedCustomers.size(), responseEntity.getBody().size());

        CustomerResponseModel expectedCustomer = expectedCustomers.get(0);
        CustomerResponseModel actualCustomer = responseEntity.getBody().get(0);
        assertEquals(expectedCustomer.getCustomerId(), actualCustomer.getCustomerId());
        assertEquals(expectedCustomer.getLastName(), actualCustomer.getLastName());

        verify(mockCustomerService, times(1)).getCustomers();
    }

}
