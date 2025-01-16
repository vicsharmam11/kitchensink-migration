package com.demo.kitchensink.exception;

public class CustomException extends RuntimeException {
    private final String message;
    private final String key;

    public CustomException(String key, String message) {
        super(message);
        this.message = message;
        this.key = key;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getKey() {
        return key;
    }
}

