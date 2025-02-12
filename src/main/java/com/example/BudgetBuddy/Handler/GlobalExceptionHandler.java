package com.example.BudgetBuddy.Handler;


import com.example.BudgetBuddy.Exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException exception) {
        return createErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException exception) {
        return createErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleGeneralException(Exception exception) {
        exception.printStackTrace();
        return createErrorResponse("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> createErrorResponse(String message, HttpStatus status) {
        HashMap<String, String> payload = new HashMap<>();
        payload.put("message", message);
        payload.put("status", "error");
        payload.put("statusCode", String.valueOf(status.value()));

        return new ResponseEntity<>(payload, status);
    }
}
