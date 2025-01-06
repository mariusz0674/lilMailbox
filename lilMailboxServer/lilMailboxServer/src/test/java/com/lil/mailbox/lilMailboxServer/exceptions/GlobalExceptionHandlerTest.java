package com.lil.mailbox.lilMailboxServer.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    @Test
    void handleUserNotExistException_shouldReturnNotFoundResponse() {
        // given
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        UserNotExistException exception = new UserNotExistException("User not exist: testUserId");

        // when
        ResponseEntity<String> response = handler.handleUserNotExistException(exception);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not exist: testUserId", response.getBody());
    }

    @Test
    void handleGenericException_shouldReturnInternalServerErrorResponse() {
        // given
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        Exception exception = new Exception("Test generic exception");

        // when
        ResponseEntity<String> response = handler.handleGenericException(exception);

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred: Test generic exception", response.getBody());
    }
}