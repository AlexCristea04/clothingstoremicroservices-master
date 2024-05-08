package com.example.orders.domainclientlayer.products;

import com.example.orders.utils.HttpErrorInfo;
import com.example.orders.utils.exceptions.InvalidInputException;
import com.example.orders.utils.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Component
public class ProductServiceClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final String PRODUCTS_SERVICE_BASE_URL;

    public ProductServiceClient(RestTemplate restTemplate, ObjectMapper mapper,
                                @Value("${app.products-service.host}") String productServiceHost,
                                @Value("${app.products-service.port}") String productServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.PRODUCTS_SERVICE_BASE_URL = "http://" + productServiceHost + ":" + productServicePort + "/api/v1/products";
    }

    public ProductModel getProductByProductId(String productId) {
        try {
            String url = PRODUCTS_SERVICE_BASE_URL + "/" + productId;

            ProductModel productModel = restTemplate.getForObject(url, ProductModel.class);

            return productModel;
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public void updateProductByProductId(String productId) {
        try {
            String url = PRODUCTS_SERVICE_BASE_URL + "/" + productId;

            restTemplate.put(url, ProductModel.class);
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        // include all possible responses from the client
        if (ex.getStatusCode() == NOT_FOUND) {
            String errorMessage = ex.getResponseBodyAsString();
            if (errorMessage.isEmpty()) {
                errorMessage = "Product not found";
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