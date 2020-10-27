package com.bestseller.ecommerce.entity;

import com.bestseller.ecommerce.model.Currency;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

	public void addCartItem(CartItem cartItem) {
		this.cartItems.stream()
				.filter(cartItem::equals)
				.findAny()
				.ifPresentOrElse(item -> item.increaseQuantityBy(cartItem.getQuantity()), () -> {
					this.cartItems.add(cartItem);
				});
	}

	public void removeCartItem(CartItem cartItem, int quantity) {
		this.cartItems.stream()
				.filter(cartItem::equals)
				.findAny()
				.ifPresent(item -> {
					if(quantity >= item.getQuantity()) {
						this.cartItems.remove(item);
					} else {
						item.decreaseQuantityBy(quantity);
					}
				});
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
		return cartItems
				.stream()
				.map(CartItem::getAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add)
				.setScale(2, RoundingMode.HALF_UP);
	}

	public BigDecimal getDiscountedAmount() {
		return discountedAmount;
	}

	public void setDiscountedAmount(BigDecimal discountedAmount) {
		if(discountedAmount == null) {
			this.discountedAmount = getTotalAmount();
		} else {
			this.discountedAmount = discountedAmount.setScale(2, RoundingMode.HALF_UP);
		}
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Cart cart = (Cart) o;
		return Objects.equals(user, cart.user);
	}

	@Override public int hashCode() {
		return Objects.hash(user);
	}
}
