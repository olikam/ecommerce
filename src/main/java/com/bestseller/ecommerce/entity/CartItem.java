package com.bestseller.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class CartItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToMany
	private List<Product> products = new ArrayList<>();

	@JsonIgnore
	@ManyToOne
	private Cart cart;

	private Integer quantity = 0;

	public CartItem() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
		increaseQuantityBy(1);
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void increaseQuantityBy(int n) {
		this.quantity += n;
	}

	public void decreaseQuantityBy(int n) {
		this.quantity = Math.max(this.quantity - n, 0);
		if(quantity == 0) {
			products = new ArrayList<>();
		}
	}

	@JsonIgnore
	public BigDecimal getAmount() {
		return products.stream()
				.map(Product::getPrice)
				.reduce(BigDecimal.ZERO, BigDecimal::add)
				.multiply(new BigDecimal(quantity))
				.setScale(2, RoundingMode.HALF_UP);
	}

	public List<Product> getProducts() {
		return products;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		CartItem cartItem = (CartItem) o;
		if(products == null && cartItem.getProducts() == null) {
			return true;
		} else if(products == null || cartItem.getProducts() == null) {
			return false;
		}
		return CollectionUtils.isEqualCollection(products, cartItem.products);
	}

	@Override
	public int hashCode() {
		return Objects.hash(products);
	}
}
