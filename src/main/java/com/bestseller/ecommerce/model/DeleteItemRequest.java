package com.bestseller.ecommerce.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class DeleteItemRequest {

	@NotNull(message = "Cart item id cannot be empty.")
	@PositiveOrZero(message = "Cart item id must be 0 at least.")
	private Long cartItemId;

	@Min(value = 1, message = "Quantity must be 1 at least.")
	private Integer quantity;

	public Long getCartItemId() {
		return cartItemId;
	}

	public void setCartItemId(Long cartItemId) {
		this.cartItemId = cartItemId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Override public String toString() {
		return "DeleteItemRequest{" + "cartItemId=" + cartItemId + ", quantity=" + quantity + '}';
	}
}
