package com.bestseller.ecommerce.model;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ProductCreateRequest {

	@NotBlank(message = "Product name cannot be empty.")
	private String name;

	@NotNull(message = "Product type cannot be empty.")
	@Enumerated(EnumType.STRING)
	private ProductType type;

	@DecimalMin(value = "0.0", message = "Price must be 0 at least.")
	private Double price;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProductType getType() {
		return type;
	}

	public void setType(ProductType type) {
		this.type = type;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
}
