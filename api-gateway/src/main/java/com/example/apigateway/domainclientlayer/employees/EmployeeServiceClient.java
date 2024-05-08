package com.example.apigateway.domainclientlayer.employees;

import com.example.apigateway.presentationlayer.employees.EmployeeRequestModel;
import com.example.apigateway.presentationlayer.employees.EmployeeResponseModel;
import com.example.apigateway.utils.HttpErrorInfo;
import com.example.apigateway.utils.exceptions.InvalidInputException;
import com.example.apigateway.utils.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Component
public class EmployeeServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final String EMPLOYEES_SERVICE_BASE_URL;

    public EmployeeServiceClient(RestTemplate restTemplate, ObjectMapper mapper,
                                 @Value("${app.employees-service.host}") String employeeServiceHost,
                                 @Value("${app.employees-service.port}") String employeeServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.EMPLOYEES_SERVICE_BASE_URL = "http://" + employeeServiceHost + ":" + employeeServicePort + "/api/v1/employees";
    }

    public List<EmployeeResponseModel> getAllEmployees() {
        try {
            String url = EMPLOYEES_SERVICE_BASE_URL;

            EmployeeResponseModel[] employeeResponseModel = restTemplate.getForObject(url, EmployeeResponseModel[].class);

            return Arrays.asList(employeeResponseModel);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public EmployeeResponseModel getEmployeeByEmployeeId(String employeeId) {
        try {
            String url = EMPLOYEES_SERVICE_BASE_URL + "/" + employeeId;

            EmployeeResponseModel employeeResponseModel = restTemplate.getForObject(url, EmployeeResponseModel.class);

            return employeeResponseModel;
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public EmployeeResponseModel createEmployee(EmployeeRequestModel employeeRequestModel) {
        try {
            String url = EMPLOYEES_SERVICE_BASE_URL;

            EmployeeResponseModel employeeResponseModel = restTemplate.postForObject(url, employeeRequestModel, EmployeeResponseModel.class);

            return employeeResponseModel;
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public EmployeeResponseModel updateEmployeeByEmployee_Id(EmployeeRequestModel employeeRequestModel, String employeeId) {
        try {
            String url = EMPLOYEES_SERVICE_BASE_URL + "/" + employeeId;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<EmployeeRequestModel> requestEntity = new HttpEntity<>(employeeRequestModel, headers);
            ResponseEntity<EmployeeResponseModel> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, EmployeeResponseModel.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return responseEntity.getBody();
            } else {
                throw new RuntimeException("Update failed with HTTP status code: " + responseEntity.getStatusCodeValue());
            }
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public void deleteEmployeeByEmployee_Id(String employeeId) {
        try {
            String url = EMPLOYEES_SERVICE_BASE_URL + "/" + employeeId;

            restTemplate.delete(url);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        if (ex.getStatusCode() == NOT_FOUND) {
            String errorMessage = ex.getResponseBodyAsString();
            if (errorMessage.isEmpty()) {
                errorMessage = "Employee not found";
            }
            return new NotFoundException(errorMessage);
        }
        if (ex.getStatusCode() == UNPROCESSABLE_ENTITY) {
            String errorMessage = ex.getResponseBodyAsString();
            if (errorMessage.isEmpty()) {
                errorMessage = "Unprocessable entity";
            }
            return new InvalidInputException(errorMessage);
        }
        if (ex.getStatusCode() == BAD_REQUEST) {
            String errorMessage = ex.getResponseBodyAsString();
            if (errorMessage.isEmpty()) {
                errorMessage = "Bad Request";
            }
            return new InvalidInputException(errorMessage);
        }
        log.warn("Got an unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
        log.warn("Error body: {}", ex.getResponseBodyAsString());
        return ex;
    }

    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (IOException ioex) {
            return ioex.getMessage();
        }
    }
}
