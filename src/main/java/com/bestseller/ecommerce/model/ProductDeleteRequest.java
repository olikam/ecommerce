package com.bestseller.ecommerce.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ProductDeleteRequest {

	@NotNull
	@Positive
	private Long productId;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
}
