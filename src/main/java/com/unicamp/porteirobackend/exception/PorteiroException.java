package com.unicamp.porteirobackend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class PorteiroException extends RuntimeException {
    private final HttpStatus status;
    private final String errorMsg;
}
