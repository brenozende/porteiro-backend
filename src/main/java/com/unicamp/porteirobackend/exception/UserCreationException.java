package com.unicamp.porteirobackend.exception;

import lombok.Getter;

@Getter
public class UserCreationException extends RuntimeException{
    private final String errorMsg;
    public UserCreationException(String errorMsg){
        this.errorMsg = errorMsg;
    }
}
