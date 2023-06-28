package com.unicamp.porteirobackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(PorteiroException.class)
    public ResponseEntity<String> handleConversion(PorteiroException ex) {
        return new ResponseEntity<>(ex.getErrorMsg(), ex.getStatus());
    }
}
