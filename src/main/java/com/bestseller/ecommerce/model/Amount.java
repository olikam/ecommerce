package com.bestseller.ecommerce.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;

public class Amount implements Comparable<Amount>{

	private BigDecimal totalAmount = new BigDecimal("0.0").setScale(2, RoundingMode.HALF_UP);

	private BigDecimal discountedAmount = new BigDecimal("0.0").setScale(2, RoundingMode.HALF_UP);

	public void addToTotal(BigDecimal amount) {
		this.totalAmount = totalAmount.add(amount);
	}

	public void addToDiscounted(BigDecimal amount) {
		this.discountedAmount = discountedAmount.add(amount);
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getDiscountedAmount() {
		return discountedAmount;
	}

	public void setDiscountedAmount(BigDecimal discountedAmount) {
		this.discountedAmount = discountedAmount;
	}

	@Override public int compareTo(Amount that) {
		return Comparator.comparing(Amount::getTotalAmount).compare(this, that);
	}
}
