package com.example.orders.domainclientlayer;

import com.example.orders.domainclientlayer.customers.CustomerModel;
import com.example.orders.domainclientlayer.customers.CustomerServiceClient;
import com.example.orders.domainclientlayer.employees.EmployeeModel;
import com.example.orders.domainclientlayer.employees.EmployeeServiceClient;
import com.example.orders.utils.exceptions.InvalidInputException;
import com.example.orders.utils.exceptions.NotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EmployeeServiceClientTest {
    private EmployeeServiceClient employeeServiceClient;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        employeeServiceClient = new EmployeeServiceClient(restTemplate, objectMapper, "host", "port");
    }

    @Test
    public void testGetEmployeeByEmployeeId() {
        // Arrange
        String employeeId = "456";
        String url = "http://host:port/api/v1/employees/" + employeeId;
        EmployeeModel expectedModel = new EmployeeModel("", "", "", "", "", new BigDecimal("0"));

        // Mocking successful retrieval
        when(restTemplate.getForObject(url, EmployeeModel.class)).thenReturn(expectedModel);

        // Act
        EmployeeModel result = employeeServiceClient.getEmployeeByEmployeeId(employeeId);

        // Assert
        assertNotNull(result);
        verify(restTemplate, times(1)).getForObject(url, EmployeeModel.class);
    }
    @Test
    public void testGetEmployeeByEmployeeId_NotFound() throws IOException {
        // Arrange
        String employeeId = "456";
        String url = "http://host:port/api/v1/employees/" + employeeId;
        String errorMessage = "Employee not found";

        // Mocking HttpClientErrorException with NOT_FOUND status
        HttpClientErrorException ex = new HttpClientErrorException(HttpStatus.NOT_FOUND, errorMessage);
        when(restTemplate.getForObject(url, EmployeeModel.class)).thenThrow(ex);

        // Act & Assert
        Exception exception = assertThrows(NotFoundException.class, () -> {
            employeeServiceClient.getEmployeeByEmployeeId(employeeId);
        });
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        verify(restTemplate, times(1)).getForObject(url, EmployeeModel.class);
    }

    @Test
    public void testGetEmployeeByEmployeeId_UnprocessableEntity() throws IOException {
        // Arrange
        String employeeId = "456";
        String url = "http://host:port/api/v1/employees/" + employeeId;
        String errorMessage = "Unprocessable entity";

        // Mocking HttpClientErrorException with UNPROCESSABLE_ENTITY status
        HttpClientErrorException ex = new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY, errorMessage);
        when(restTemplate.getForObject(url, EmployeeModel.class)).thenThrow(ex);

        // Act & Assert
        Exception exception = assertThrows(InvalidInputException.class, () -> {
            employeeServiceClient.getEmployeeByEmployeeId(employeeId);
        });
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        verify(restTemplate, times(1)).getForObject(url, EmployeeModel.class);
    }

    @Test
    public void testGetEmployeeByEmployee_BadRequest() {
        // Arrange
        String employeeId = "123";
        String url = "http://host:port/api/v1/employees/" + employeeId;
        String errorMessage = "Bad Request";

        HttpClientErrorException ex = new HttpClientErrorException(HttpStatus.BAD_REQUEST, errorMessage);
        when(restTemplate.getForObject(url, EmployeeModel.class)).thenThrow(ex);

        // Act & Assert
        Exception exception = assertThrows(InvalidInputException.class, () -> {
            employeeServiceClient.getEmployeeByEmployeeId(employeeId);
        });
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        verify(restTemplate, times(1)).getForObject(url, EmployeeModel.class);
    }

    @Test
    public void testGetEmployeeByEmployeeId_NoValidError() {
        // Arrange
        String employeeId = "123";
        String url = "http://host:port/api/v1/employees/" + employeeId;
        String errorMessage = "No valid error";

        HttpClientErrorException ex = new HttpClientErrorException(HttpStatus.CREATED, errorMessage);
        when(restTemplate.getForObject(url, EmployeeModel.class)).thenThrow(ex);

        // Act & Assert
        Exception exception = assertThrows(HttpClientErrorException.class, () -> {
            employeeServiceClient.getEmployeeByEmployeeId(employeeId);
        });
        assertNotNull(exception);
        assertEquals("201 " + errorMessage, exception.getMessage());
        verify(restTemplate, times(1)).getForObject(url, EmployeeModel.class);
    }


}
