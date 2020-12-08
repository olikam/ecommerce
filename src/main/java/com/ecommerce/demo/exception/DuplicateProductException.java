package com.ecommerce.demo.exception;

public class DuplicateProductException extends RuntimeException {

    public DuplicateProductException(String productName) {
        super(productName);
    }
}
