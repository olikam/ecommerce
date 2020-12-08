package com.ecommerce.demo.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long productId) {
        super(productId.toString());
    }

}
