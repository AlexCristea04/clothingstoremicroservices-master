package com.example.employees.presentationlayer;

import com.example.employees.businesslayer.EmployeeServiceImpl;
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

@SpringBootTest(classes = EmployeeController.class)
class ClientControllerUnitTest {

    private final String FOUND_EMPLOYEE_ID = "123e4567-a59c-12d3-a456-556642440000";
    private final String NOT_FOUND_EMPLOYEE_ID = "c3333333-3333-3333-444444444444";
    private final String INVALID_FOUND_EMPLOYEE_ID = "c3333333-3333-3333";

    @Autowired
    EmployeeController employeeController;

    @MockBean
    private EmployeeServiceImpl employeeService;

    @Test
    public void whenNoEmployeeExists_thenReturnEmptyList(){
        //arrange
        when(employeeService.getEmployees()).thenReturn(Collections.emptyList());

        //act
        ResponseEntity<List<EmployeeResponseModel>> responseEntity = employeeController.getEmployees();

        //assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().isEmpty());

        verify(employeeService, times(1)).getEmployees();
    }

    @Test
    public void whenEmployeeExists_thenReturnClient(){
        //arrange
        EmployeeRequestModel employeeRequestModel = buildEmployeeRequestModel();
        EmployeeResponseModel employeeResponseModel = buildEmployeeResponseModel();

        when(employeeService.addEmployee(employeeRequestModel)).thenReturn(employeeResponseModel);

        //act
        ResponseEntity<EmployeeResponseModel> responseEntity = employeeController.createEmployee(employeeRequestModel);

        //assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(employeeResponseModel, responseEntity.getBody());

        verify(employeeService, times(1)).addEmployee(employeeRequestModel);
    }

    private EmployeeRequestModel buildEmployeeRequestModel(){
        return EmployeeRequestModel.builder()
                .employeeId(FOUND_EMPLOYEE_ID)
                .lastName("Doe")
                .firstName("John")
                .jobTitle("Sales Associate")
                .department("Sales")
                .salary(new BigDecimal("30000.00"))
                .build();
    }

    private EmployeeResponseModel buildEmployeeResponseModel(){
        return EmployeeResponseModel.builder()
                .employeeId(FOUND_EMPLOYEE_ID)
                .lastName("Doe")
                .firstName("John")
                .jobTitle("Sales Associate")
                .department("Sales")
                .salary(new BigDecimal("30000.00"))
                .build();
    }
}