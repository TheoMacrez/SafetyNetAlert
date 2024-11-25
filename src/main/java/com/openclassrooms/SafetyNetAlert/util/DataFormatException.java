package com.openclassrooms.SafetyNetAlert.util;

public class DataFormatException extends RuntimeException {
    public DataFormatException(String message) {
        super(message);
    }

    public DataFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}

