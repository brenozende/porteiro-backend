package com.unicamp.porteirobackend.exception;

import lombok.Getter;

@Getter
public class BookingCreationException extends RuntimeException{
    private final String errorMsg;

    public BookingCreationException(String errorMsg){
        this.errorMsg = errorMsg;
    }
}
