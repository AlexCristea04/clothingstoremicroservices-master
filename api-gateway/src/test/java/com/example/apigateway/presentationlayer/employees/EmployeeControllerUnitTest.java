package com.example.apigateway.presentationlayer.employees;

import com.example.apigateway.businesslayer.employees.EmployeeService;
import com.example.apigateway.businesslayer.employees.EmployeeServiceImpl;
import com.example.apigateway.domainclientlayer.employees.EmployeeServiceClient;
import com.example.apigateway.mapperlayer.employees.EmployeeResponseMapper;
import com.example.apigateway.mapperlayer.employees.EmployeeResponseMapperImpl;
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

public class EmployeeControllerUnitTest {

    private EmployeeController employeeController;
    private EmployeeResponseMapper mockEmployeeResponseMapper;
    private EmployeeService mockEmployeeService;

    @BeforeEach
    void setUp() {
        mockEmployeeService = mock(EmployeeService.class);
        mockEmployeeResponseMapper = mock(EmployeeResponseMapper.class);
        employeeController = new EmployeeController(mockEmployeeService);
    }

    @Test
    void testCreateEmployee() {
        // Arrange
        EmployeeRequestModel requestModel = new EmployeeRequestModel();
        EmployeeResponseModel expectedResponse = new EmployeeResponseModel();
        expectedResponse.setEmployeeId(UUID.randomUUID().toString());
        when(mockEmployeeService.addEmployee(requestModel)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<EmployeeResponseModel> result = employeeController.createEmployee(requestModel);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(expectedResponse, result.getBody());
    }

    @Test
    void testUpdateEmployee() {
        // Arrange
        String employeeId = "123";
        EmployeeRequestModel requestModel = new EmployeeRequestModel();
        EmployeeResponseModel expectedResponse = new EmployeeResponseModel();
        expectedResponse.setEmployeeId(UUID.randomUUID().toString());
        when(mockEmployeeService.updateEmployee(eq(requestModel), eq(employeeId))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<EmployeeResponseModel> result = employeeController.updateEmployee(requestModel, employeeId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(expectedResponse, result.getBody());
    }

    @Test
    void testDeleteEmployee() {
        // Arrange
        String employeeId = "123";

        // Act
        employeeController.deleteEmployee(employeeId);

        // Assert
        verify(mockEmployeeService, times(1)).removeEmployee(employeeId);
    }

    @Test
    void testGetEmployeeById() {
        // Arrange
        String employeeId = "123";
        EmployeeResponseModel expectedResponse = new EmployeeResponseModel();
        when(mockEmployeeService.getEmployeeByEmployeeId(employeeId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<EmployeeResponseModel> responseEntity = employeeController.getEmployeeByEmployeeId(employeeId);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void testGetEmployees() {
        // Arrange
        List<EmployeeResponseModel> expectedEmployees = Collections.singletonList(new EmployeeResponseModel());
        when(mockEmployeeService.getEmployees()).thenReturn(expectedEmployees);

        // Act
        ResponseEntity<List<EmployeeResponseModel>> responseEntity = employeeController.getEmployees();

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(expectedEmployees.size(), responseEntity.getBody().size());

        EmployeeResponseModel expectedEmployee = expectedEmployees.get(0);
        EmployeeResponseModel actualEmployee = responseEntity.getBody().get(0);
        assertEquals(expectedEmployee.getEmployeeId(), actualEmployee.getEmployeeId());
        assertEquals(expectedEmployee.getLastName(), actualEmployee.getLastName());

        verify(mockEmployeeService, times(1)).getEmployees();
    }
}
