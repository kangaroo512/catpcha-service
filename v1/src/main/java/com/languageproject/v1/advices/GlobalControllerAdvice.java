package com.languageproject.v1.advices;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import jakarta.mail.MessagingException;

@RestControllerAdvice
public class GlobalControllerAdvice {


    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(
        MethodArgumentNotValidException ex
    ) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
        .forEach(
            err -> errors.put(err.getField(), err.getDefaultMessage())
            );
        
            return ResponseEntity.badRequest().body(errors);
    }

}
