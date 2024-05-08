package com.example.employees.utils;


import com.example.employees.utils.exceptions.EmployeeNotFoundException;
import com.example.employees.utils.exceptions.InUseException;
import com.example.employees.utils.exceptions.InvalidInputException;
import com.example.employees.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GlobalControllerExceptionHandlerTest {

    private final GlobalControllerExceptionHandler handler = new GlobalControllerExceptionHandler();

    @Test
    void handleNotFoundException() {
        NotFoundException ex = new NotFoundException("Not found");
        WebRequest request = mock(WebRequest.class);
        HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

        assertEquals(expectedStatus, handler.handleNotFoundException(request, ex).getHttpStatus());
        assertNotNull(handler.handleNotFoundException(request, ex).getMessage());
    }

    @Test
    void handleInUseException() {
        InUseException ex = new InUseException("In use");
        WebRequest request = mock(WebRequest.class);
        HttpStatus expectedStatus = HttpStatus.UNPROCESSABLE_ENTITY;

        assertEquals(expectedStatus, handler.handleInUseException(request, ex).getHttpStatus());
        assertNotNull(handler.handleInUseException(request, ex).getMessage());
    }

    @Test
    void handleInvalidInputException() {
        InvalidInputException ex = new InvalidInputException("Invalid input");
        WebRequest request = mock(WebRequest.class);
        HttpStatus expectedStatus = HttpStatus.UNPROCESSABLE_ENTITY;

        assertEquals(expectedStatus, handler.handleInvalidInputException(request, ex).getHttpStatus());
        assertNotNull(handler.handleInvalidInputException(request, ex).getMessage());
    }


    @Test
    void handleCustomerNotFoundException() {
        EmployeeNotFoundException ex = new EmployeeNotFoundException("Not found");
        WebRequest request = mock(WebRequest.class);
        HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

        assertEquals(expectedStatus, handler.handleNotFoundException(request, ex).getHttpStatus());
        assertNotNull(handler.handleNotFoundException(request, ex).getMessage());
    }

    @Test
    void handleInvalidInputExceptionWithMessageAndCause() {
        String message = "Invalid input";
        Throwable cause = new Throwable("Cause of invalid input");
        InvalidInputException ex = new InvalidInputException(message, cause);
        WebRequest request = mock(WebRequest.class);
        HttpStatus expectedStatus = HttpStatus.UNPROCESSABLE_ENTITY;

        assertEquals(expectedStatus, handler.handleInvalidInputException(request, ex).getHttpStatus());
        assertNotNull(handler.handleInvalidInputException(request, ex).getMessage());
    }

    @Test
    void handleInvalidInputExceptionWithNoMessage() {
        InvalidInputException ex = new InvalidInputException();
        WebRequest request = mock(WebRequest.class);
        HttpStatus expectedStatus = HttpStatus.UNPROCESSABLE_ENTITY;

        assertEquals(expectedStatus, handler.handleInvalidInputException(request, ex).getHttpStatus());
        assertNull(handler.handleInvalidInputException(request, ex).getMessage());
    }

    @Test
    void handleInvalidInputExceptionWithCause() {
        Throwable cause = new Throwable("Cause of invalid input");
        InvalidInputException ex = new InvalidInputException(cause);
        WebRequest request = mock(WebRequest.class);
        HttpStatus expectedStatus = HttpStatus.UNPROCESSABLE_ENTITY;

        assertEquals(expectedStatus, handler.handleInvalidInputException(request, ex).getHttpStatus());
        assertNotNull(handler.handleInvalidInputException(request, ex).getMessage());
    }

    @Test
    void handleInUseExceptionWithMessageAndCause() {
        String message = "In use";
        Throwable cause = new Throwable("Cause of in use");
        InUseException ex = new InUseException(message, cause);
        WebRequest request = mock(WebRequest.class);
        HttpStatus expectedStatus = HttpStatus.UNPROCESSABLE_ENTITY;

        assertEquals(expectedStatus, handler.handleInUseException(request, ex).getHttpStatus());
        assertNotNull(handler.handleInUseException(request, ex).getMessage());
    }

    @Test
    void handleInUseExceptionWithCause() {
        Throwable cause = new Throwable("Cause of in use");
        InUseException ex = new InUseException(cause);
        WebRequest request = mock(WebRequest.class);
        HttpStatus expectedStatus = HttpStatus.UNPROCESSABLE_ENTITY;

        assertEquals(expectedStatus, handler.handleInUseException(request, ex).getHttpStatus());
        assertNotNull(handler.handleInUseException(request, ex).getMessage());
    }

    @Test
    void handleInUseExceptionWithNoMessage() {
        InUseException ex = new InUseException();
        WebRequest request = mock(WebRequest.class);
        HttpStatus expectedStatus = HttpStatus.UNPROCESSABLE_ENTITY;

        assertEquals(expectedStatus, handler.handleInUseException(request, ex).getHttpStatus());
        assertNull(handler.handleInUseException(request, ex).getMessage());
    }

    @Test
    void handleNotFoundExceptionWithMessageAndCause() {
        String message = "Not found";
        Throwable cause = new Throwable("Cause of not found");
        NotFoundException ex = new NotFoundException(message, cause);
        WebRequest request = mock(WebRequest.class);
        HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

        assertEquals(expectedStatus, handler.handleNotFoundException(request, ex).getHttpStatus());
        assertNotNull(handler.handleNotFoundException(request, ex).getMessage());
    }

    @Test
    void handleNotFoundExceptionWithCause() {
        Throwable cause = new Throwable("Cause of not found");
        NotFoundException ex = new NotFoundException(cause);
        WebRequest request = mock(WebRequest.class);
        HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

        assertEquals(expectedStatus, handler.handleNotFoundException(request, ex).getHttpStatus());
        assertNotNull(handler.handleNotFoundException(request, ex).getMessage());
    }

    @Test
    void handleNotFoundExceptionWithNoMessage() {
        NotFoundException ex = new NotFoundException();
        WebRequest request = mock(WebRequest.class);
        HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

        assertEquals(expectedStatus, handler.handleNotFoundException(request, ex).getHttpStatus());
        assertNull(handler.handleNotFoundException(request, ex).getMessage());
    }

    @Test
    void handleCustomerNotFoundExceptionWithMessageAndCause() {
        String message = "Employee not found";
        Throwable cause = new Throwable("Cause of employee not found");
        EmployeeNotFoundException ex = new EmployeeNotFoundException(message, cause);
        WebRequest request = mock(WebRequest.class);
        HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

        assertEquals(expectedStatus, handler.handleEmployeeNotFoundException(request, ex).getHttpStatus());
        assertNotNull(handler.handleEmployeeNotFoundException(request, ex).getMessage());
    }

    @Test
    void handleCustomerNotFoundExceptionWithCause() {
        Throwable cause = new Throwable("Cause of employee not found");
        EmployeeNotFoundException ex = new EmployeeNotFoundException(cause);
        WebRequest request = mock(WebRequest.class);
        HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

        assertEquals(expectedStatus, handler.handleEmployeeNotFoundException(request, ex).getHttpStatus());
        assertNotNull(handler.handleEmployeeNotFoundException(request, ex).getMessage());
    }

    @Test
    void handleCustomerNotFoundExceptionNoMessage() {
        EmployeeNotFoundException ex = new EmployeeNotFoundException();
        WebRequest request = mock(WebRequest.class);
        HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

        assertEquals(expectedStatus, handler.handleEmployeeNotFoundException(request, ex).getHttpStatus());
        assertNull(handler.handleEmployeeNotFoundException(request, ex).getMessage());
    }


}