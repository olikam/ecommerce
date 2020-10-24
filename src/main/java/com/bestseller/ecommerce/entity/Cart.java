package com.bestseller.ecommerce.entity;

import com.bestseller.ecommerce.model.Currency;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	@OneToOne
	@JsonIgnore
	private User user;

	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CartItem> cartItems = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	private Currency currency = Currency.EUR;

	@Transient
	private BigDecimal totalAmount;

	@Transient
	private BigDecimal discountedAmount;

	public Cart() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<CartItem> getCartItems() {
		return new ArrayList<>(this.cartItems);
	}

	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	public void addCartItem(CartItem cartItem, int quantity) {
		this.cartItems.stream()
				.filter(cartItem::equals)
				.findAny()
				.ifPresentOrElse(item -> item.increaseQuantityBy(quantity), () -> this.cartItems.add(cartItem));
	}

	public void removeCartItem(CartItem cartItem, int quantity) {
		this.cartItems.stream()
				.filter(cartItem::equals)
				.findAny()
				.ifPresentOrElse(item -> item.decreaseQuantityBy(quantity), () -> this.cartItems.remove(cartItem));
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount.setScale(2, RoundingMode.HALF_UP);
	}

	public BigDecimal getDiscountedAmount() {
		return discountedAmount;
	}

	public void setDiscountedAmount(BigDecimal discountedAmount) {
		this.discountedAmount = discountedAmount.setScale(2, RoundingMode.HALF_UP);
	}

}
