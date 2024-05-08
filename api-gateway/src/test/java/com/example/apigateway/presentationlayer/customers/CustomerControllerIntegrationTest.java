package com.example.apigateway.presentationlayer.customers;

import com.example.apigateway.businesslayer.customers.CustomerServiceImpl;
import com.example.apigateway.domainclientlayer.customers.CustomerServiceClient;
import com.example.apigateway.mapperlayer.customers.CustomerResponseMapper;
import com.example.apigateway.mapperlayer.customers.CustomerResponseMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class CustomerControllerIntegrationTest {
    private CustomerServiceClient mockClient;
    private CustomerResponseMapper mockMapper;
    private CustomerServiceImpl customerService;
    private CustomerResponseMapper customerResponseMapper;

    @BeforeEach
    void setUp() {
        mockClient = mock(CustomerServiceClient.class);
        mockMapper = mock(CustomerResponseMapper.class);
        customerResponseMapper = new CustomerResponseMapperImpl();
        customerService = new CustomerServiceImpl(mockClient, mockMapper);
    }

    @Test
    void testGetAllHusbands() {
        // Arrange
        List<CustomerResponseModel> customers = Collections.singletonList(new CustomerResponseModel());
        List<CustomerResponseModel> expectedResponseModels = Collections.singletonList(new CustomerResponseModel());
        when(mockClient.getAllCustomers()).thenReturn(customers);
        when(mockMapper.responseModelListToResponseModelList(customers)).thenReturn(expectedResponseModels);

        // Act
        List<CustomerResponseModel> result = customerService.getCustomers();

        // Assert
        assertEquals(expectedResponseModels.size(), result.size());
    }

    @Test
    void testGetCustomerByCustomerId() {
        // Arrange
        String customerId = "123";

        CustomerResponseModel customer = new CustomerResponseModel();
        CustomerResponseModel expectedResponseModel = new CustomerResponseModel();

        when(mockClient.getCustomerByCustomerId(customerId)).thenReturn(customer);
        when(mockMapper.responseModelToResponseModel(customer)).thenReturn(expectedResponseModel);

        // Act
        CustomerResponseModel result = customerService.getCustomerByCustomerId(customerId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponseModel, result);
    }

    @Test
    void testCreateCustomer() {
        // Arrange
        CustomerRequestModel requestModel = new CustomerRequestModel();
        CustomerResponseModel customer = new CustomerResponseModel();
        CustomerResponseModel expectedResponseModel = new CustomerResponseModel();

        when(mockClient.createCustomer(requestModel)).thenReturn(customer);
        when(mockMapper.responseModelToResponseModel(customer)).thenReturn(expectedResponseModel);

        // Act
        CustomerResponseModel result = customerService.addCustomer(requestModel);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponseModel, result);
    }

    @Test
    void testUpdateCustomer() {
        // Arrange
        String customerId = "123";
        CustomerRequestModel requestModel = new CustomerRequestModel();
        CustomerResponseModel customer = new CustomerResponseModel();
        CustomerResponseModel expectedResponseModel = new CustomerResponseModel();

        when(mockClient.updateCustomerByCustomer_Id(requestModel, customerId)).thenReturn(customer);
        when(mockMapper.responseModelToResponseModel(customer)).thenReturn(expectedResponseModel);

        // Act
        CustomerResponseModel result = customerService.updateCustomer(requestModel, customerId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponseModel, result);
    }

    @Test
    void testDeleteCustomerByCustomerId() {
        // Arrange
        String customerId = "123";

        // Act
        customerService.removeCustomer(customerId);

        // Assert
        verify(mockClient, times(1)).deleteCustomerByCustomer_Id(customerId);
    }

    @Test
    void testMapRequestToResponse() {
        // Arrange
        CustomerResponseModel responseModel = new CustomerResponseModel();
        responseModel.setCustomerId("1");
        responseModel.setLastName("Name");

        // Act
        CustomerResponseModel responseModel2 = customerResponseMapper.responseModelToResponseModel(responseModel);

        // Assert
        assertNotNull(responseModel2);
        assertEquals(responseModel2.getCustomerId(), responseModel.getCustomerId());
        assertEquals(responseModel2.getLastName(), responseModel.getLastName());
    }

}
