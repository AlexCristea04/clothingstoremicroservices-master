package com.example.employees.presentationlayer;

import com.example.employees.datalayer.EmployeeRepository;
import com.example.employees.mapperlayer.EmployeeRequestMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("h2")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ClientControllerIntegrationTest {

    private final String BASE_URI_EMPLOYEES = "api/v1/employees";
    private final String FOUND_EMPLOYEE_ID = "123e4567-a59c-12d3-a456-556642440000";
    private final String FOUND_EMPLOYEE_LAST_NAME = "Doe";
    private final String NOT_FOUND_EMPLOYEE_ID = "c3333333-3333-3333-444444444444";

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void whenGetClients_thenReturnAllClients(){

        long sizeDB = employeeRepository.count();
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!"+ employeeRepository.count());
        //act & assert
        webTestClient.get().uri(BASE_URI_EMPLOYEES)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(EmployeeResponseModel.class)
                .value((list) -> {
                    assertNotNull(list);
                    assertEquals(list.size(), sizeDB);
                });
    }

    @Test
    public void whenGetClientDoesNotExist_thenReturnNotFound(){
        //act & assert
        webTestClient.get().uri(BASE_URI_EMPLOYEES + "/" + NOT_FOUND_EMPLOYEE_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown employeeId: " + NOT_FOUND_EMPLOYEE_ID);
    }

    @Test
    public void whenValidClient_thenCreateClient(){
        //arrange
        long sizeDB = employeeRepository.count();

        EmployeeRequestModel employeeRequestModel = new EmployeeRequestModel(FOUND_EMPLOYEE_ID,
                "Doe",
                "John",
                "Sales Associate",
                "Sales",
                new BigDecimal("30000.00")
        );

        webTestClient.post()
                .uri(BASE_URI_EMPLOYEES)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(employeeRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.valueOf("application/hal+json"))
                .expectBody(EmployeeResponseModel.class)
                .value((employeeResponseModel) -> {
                    assertNotNull(employeeRequestModel);
                    assertEquals(employeeRequestModel.getLastName(), employeeResponseModel.getLastName());
                    assertEquals(employeeRequestModel.getFirstName(), employeeResponseModel.getFirstName());
                    assertEquals(employeeRequestModel.getJobTitle(), employeeResponseModel.getJobTitle());
                    assertEquals(employeeRequestModel.getDepartment(), employeeResponseModel.getDepartment());
                    assertEquals(employeeRequestModel.getSalary(), employeeResponseModel.getSalary());
                });

        long sizeDBAfter = employeeRepository.count();
        assertEquals(sizeDB + 1, sizeDBAfter);
    }

    @Test
    public void whenUpdateClient_thenReturnUpdatedClient() {
        // Arrange
        EmployeeRequestModel employeeToUpdate = new EmployeeRequestModel(FOUND_EMPLOYEE_ID,
                "Doe",
                "John",
                "Sales Associate",
                "Sales",
                new BigDecimal("30000.00"));


        // Act & Assert
        webTestClient.put()
                .uri(BASE_URI_EMPLOYEES + "/" + FOUND_EMPLOYEE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(employeeToUpdate)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.valueOf("application/hal+json"))
                .expectBody(EmployeeResponseModel.class)
                .value((updatedEmployee) -> {
                    assertNotNull(updatedEmployee);
                    assertEquals(employeeToUpdate.getLastName(), updatedEmployee.getLastName());
                    assertEquals(employeeToUpdate.getFirstName(), updatedEmployee.getFirstName());
                    assertEquals(employeeToUpdate.getJobTitle(), updatedEmployee.getJobTitle());
                    assertEquals(employeeToUpdate.getDepartment(), updatedEmployee.getDepartment());
                    assertEquals(employeeToUpdate.getSalary(), updatedEmployee.getSalary());
                });
    }

    @Test
    public void whenUpdateNonExistentClient_thenThrowNotFoundException() {
        // Arrange
        String nonExistentEmployeeId = "nonExistentId";
        EmployeeRequestModel updatedEmployee = new EmployeeRequestModel(nonExistentEmployeeId, "UpdateLastName",
                "UpdateFirstName",
                "UpdateJobTitle",
                "UpdateDepartment",
                new BigDecimal("30000.00")
        );

        // Act & Assert
        webTestClient.put()
                .uri(BASE_URI_EMPLOYEES + "/" + nonExistentEmployeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedEmployee)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown employeeId: " + nonExistentEmployeeId);
    }

    @Test
    public void whenRemoveNonExistentClient_thenThrowNotFoundException() {
        // Arrange
        String nonExistentClientId = "nonExistentId";

        // Act & Assert
        webTestClient.delete().uri(BASE_URI_EMPLOYEES + "/" + nonExistentClientId)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown employeeId: " + nonExistentClientId);
    }


    @Test
    public void whenDeleteClient_thenDeleteClientSuccessfully() {
        // Act
        webTestClient.delete().uri(BASE_URI_EMPLOYEES + "/" + FOUND_EMPLOYEE_ID)
                .exchange()
                .expectStatus()
                .isNoContent();

        //Assert
        assertFalse(employeeRepository.existsEmployeeByEmployeeIdentifier_EmployeeId(FOUND_EMPLOYEE_ID));
    }

    @Test
    public void whenGetClientById_thenReturnClient() {
        // Act & Assert
        webTestClient.get().uri(BASE_URI_EMPLOYEES + "/" + FOUND_EMPLOYEE_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeResponseModel.class)
                .value((employee) -> {
                    assertNotNull(employee);
                    assertEquals(FOUND_EMPLOYEE_ID, employee.getEmployeeId());
                    assertEquals(FOUND_EMPLOYEE_LAST_NAME, employee.getLastName());
                });
    }


}