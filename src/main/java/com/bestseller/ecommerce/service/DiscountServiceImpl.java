package com.bestseller.ecommerce.service;

import com.bestseller.ecommerce.entity.Cart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;

@Service
@Transactional
public class DiscountServiceImpl implements DiscountService {

	@Override
	public void apply(Cart cart) {
		BigDecimal discountedAmount = Arrays.stream(DiscountRules.values())
				.map(rule -> rule.calculate(cart))
				.min(Comparator.naturalOrder())
				.orElse(cart.getTotalAmount())
				.setScale(2, RoundingMode.HALF_UP);
		cart.setDiscountedAmount(discountedAmount);
	}
}
