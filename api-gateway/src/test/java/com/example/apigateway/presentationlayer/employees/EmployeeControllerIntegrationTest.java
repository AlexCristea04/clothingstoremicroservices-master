package com.example.apigateway.presentationlayer.employees;

import com.example.apigateway.businesslayer.employees.EmployeeServiceImpl;
import com.example.apigateway.domainclientlayer.employees.EmployeeServiceClient;
import com.example.apigateway.mapperlayer.employees.EmployeeResponseMapper;
import com.example.apigateway.mapperlayer.employees.EmployeeResponseMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class EmployeeControllerIntegrationTest {
    private EmployeeServiceClient mockClient;
    private EmployeeResponseMapper mockMapper;
    private EmployeeServiceImpl employeeService;
    private EmployeeResponseMapper employeeResponseMapper;

    @BeforeEach
    void setUp() {
        mockClient = mock(EmployeeServiceClient.class);
        mockMapper = mock(EmployeeResponseMapper.class);
        employeeResponseMapper = new EmployeeResponseMapperImpl();
        employeeService = new EmployeeServiceImpl(mockClient, mockMapper);
    }

    @Test
    void testGetAllEmployees() {
        // Arrange
        List<EmployeeResponseModel> employees = Collections.singletonList(new EmployeeResponseModel());
        List<EmployeeResponseModel> expectedResponseModels = Collections.singletonList(new EmployeeResponseModel());
        when(mockClient.getAllEmployees()).thenReturn(employees);
        when(mockMapper.responseModelListToResponseModelList(employees)).thenReturn(expectedResponseModels);

        // Act
        List<EmployeeResponseModel> result = employeeService.getEmployees();

        // Assert
        assertEquals(expectedResponseModels.size(), result.size());
    }

    @Test
    void testGetEmployeeByEmployeeId() {
        // Arrange
        String employeeId = "123";

        EmployeeResponseModel employee = new EmployeeResponseModel();
        EmployeeResponseModel expectedResponseModel = new EmployeeResponseModel();

        when(mockClient.getEmployeeByEmployeeId(employeeId)).thenReturn(employee);
        when(mockMapper.responseModelToResponseModel(employee)).thenReturn(expectedResponseModel);

        // Act
        EmployeeResponseModel result = employeeService.getEmployeeByEmployeeId(employeeId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponseModel, result);
    }

    @Test
    void testCreateEmployee() {
        // Arrange
        EmployeeRequestModel requestModel = new EmployeeRequestModel();
        EmployeeResponseModel employee = new EmployeeResponseModel();
        EmployeeResponseModel expectedResponseModel = new EmployeeResponseModel();

        when(mockClient.createEmployee(requestModel)).thenReturn(employee);
        when(mockMapper.responseModelToResponseModel(employee)).thenReturn(expectedResponseModel);

        // Act
        EmployeeResponseModel result = employeeService.addEmployee(requestModel);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponseModel, result);
    }

    @Test
    void testUpdateEmployee() {
        // Arrange
        String employeeId = "123";
        EmployeeRequestModel requestModel = new EmployeeRequestModel();
        EmployeeResponseModel employee = new EmployeeResponseModel();
        EmployeeResponseModel expectedResponseModel = new EmployeeResponseModel();

        when(mockClient.updateEmployeeByEmployee_Id(requestModel, employeeId)).thenReturn(employee);
        when(mockMapper.responseModelToResponseModel(employee)).thenReturn(expectedResponseModel);

        // Act
        EmployeeResponseModel result = employeeService.updateEmployee(requestModel, employeeId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponseModel, result);
    }

    @Test
    void testDeleteEmployeeByEmployeeId() {
        // Arrange
        String employeeId = "123";

        // Act
        employeeService.removeEmployee(employeeId);

        // Assert
        verify(mockClient, times(1)).deleteEmployeeByEmployee_Id(employeeId);
    }

    @Test
    void testMapRequestToResponse() {
        // Arrange
        EmployeeResponseModel responseModel = new EmployeeResponseModel();
        responseModel.setEmployeeId("1");
        responseModel.setLastName("Name");

        // Act
        EmployeeResponseModel responseModel2 = employeeResponseMapper.responseModelToResponseModel(responseModel);

        // Assert
        assertNotNull(responseModel2);
        assertEquals(responseModel2.getEmployeeId(), responseModel.getEmployeeId());
        assertEquals(responseModel2.getLastName(), responseModel.getLastName());
    }
}
