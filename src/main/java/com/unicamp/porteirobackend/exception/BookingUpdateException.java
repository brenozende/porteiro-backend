package com.unicamp.porteirobackend.exception;

import lombok.Getter;

@Getter
public class BookingUpdateException extends RuntimeException {
    private final String errorMsg;

    public BookingUpdateException(String errorMsg){
        this.errorMsg = errorMsg;
    }
}
