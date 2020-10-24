package com.bestseller.ecommerce.exception;

public class DuplicateProductException extends RuntimeException {

	public DuplicateProductException(String productName) {
		super(productName);
	}
}
