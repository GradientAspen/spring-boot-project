package com.project.springbootproject.exception;

public class ShoppingCartNotFoundException extends RuntimeException {
    public ShoppingCartNotFoundException(String message) {
        super(message);
    }
}
