package com.unicamp.porteirobackend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookingDeletionException extends RuntimeException {
    private final String errorMsg;
}
