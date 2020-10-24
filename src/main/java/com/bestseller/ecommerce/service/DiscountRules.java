package com.bestseller.ecommerce.service;

import com.bestseller.ecommerce.entity.Cart;
import com.bestseller.ecommerce.entity.CartItem;
import com.bestseller.ecommerce.entity.Product;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.function.Function;

public enum DiscountRules {

	RULE1 {
		@Override
		public BigDecimal calculate(Cart cart) {
			if (cart.getTotalAmount().doubleValue() > 12) {
				return cart.getTotalAmount().multiply(new BigDecimal("0.75"));
			}
			return cart.getTotalAmount();
		}
	},
	RULE2 {
		@Override
		public BigDecimal calculate(Cart cart) {
			if(cart.getCartItems().stream().mapToInt(CartItem::getQuantity).sum() >= 3) {
				BigDecimal minAmount = cart.getCartItems().stream()
						.map(getLowestPrice())
						.min(Comparator.naturalOrder())
						.orElse(BigDecimal.ZERO);
				return cart.getTotalAmount().subtract(minAmount);
			}
			return cart.getTotalAmount();
		}

		private Function<CartItem, BigDecimal> getLowestPrice() {
			return cartItem -> cartItem.getProducts().stream()
					.map(Product::getPrice)
					.min(Comparator.naturalOrder())
					.orElse(BigDecimal.ZERO);
		}
	};

	public abstract BigDecimal calculate(Cart cart);

}
