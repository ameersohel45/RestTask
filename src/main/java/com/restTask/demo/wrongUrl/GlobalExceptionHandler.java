package com.restTask.demo.wrongUrl;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle incorrect URL access (NoHandlerFoundException)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "status", HttpStatus.NOT_FOUND.value(),
                "message", "The requested URL was not found on this server."
        ));
    }
    @ExceptionHandler({CannotCreateTransactionException.class, DataAccessException.class})
    public ResponseEntity<String> handleDatabaseConnectionException(Exception ex) {
        return new ResponseEntity<>("Database connection failed. Please check the database server.", HttpStatus.SERVICE_UNAVAILABLE);
    }
    
 

    
}
