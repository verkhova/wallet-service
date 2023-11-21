package com.gfl.walletservice.exception;


import lombok.Value;

@Value
public class ResourceNotFoundException extends RuntimeException {

    private final String message;
}
