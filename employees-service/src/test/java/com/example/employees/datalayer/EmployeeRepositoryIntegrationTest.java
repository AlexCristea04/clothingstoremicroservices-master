package com.example.employees.datalayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class EmployeeRepositoryIntegrationTest {
    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setUpDb() { employeeRepository.deleteAll(); }

    @Test
    public void whenEmployeeExists_ReturnEmployeeByClientId(){
        // Initialize Employee
        Employee employee = new Employee(
                "Fucker", // Last Name
                "John", // First Name
                "asijfi.doe@example.com", // Email Address
                "Sales Associate",
                new BigDecimal("20000.00")
        );

        employeeRepository.save(employee);

        //act
        Employee employeeFound = employeeRepository.findByEmployeeIdentifier_EmployeeId(employee.getEmployeeIdentifier().getEmployeeId());

        //assert
        assertNotNull(employeeFound);
        assertEquals(employee, employeeFound);

    }
}
