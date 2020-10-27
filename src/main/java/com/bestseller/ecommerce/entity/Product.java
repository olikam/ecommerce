package com.bestseller.ecommerce.entity;

import com.bestseller.ecommerce.model.ProductType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Entity
public class Product implements Comparable<Product> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique=true)
	@NotNull(message = "Name is mandatory")
	private String name;

	@NotNull(message = "Price is mandatory")
	private BigDecimal price;

	@NotNull(message = "Product type is mandatory")
	@Enumerated(EnumType.STRING)
	private ProductType type;

	@JsonIgnore
	@ManyToMany(mappedBy = "products", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<CartItem> cartItem = new ArrayList<>();

	public Product() {
	}

	public Product(@NotNull(message = "Name is mandatory") String name, @NotNull(message = "Price is mandatory") BigDecimal price,
			@NotNull(message = "Product type is mandatory") ProductType type) {
		this.name = name;
		this.price = price;
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(@NotNull(message = "ID is mandatory") Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price.setScale(2, RoundingMode.HALF_UP);
	}

	public ProductType getType() {
		return type;
	}

	public void setType(ProductType type) {
		this.type = type;
	}

	public List<CartItem> getCartItem() {
		return cartItem;
	}

	public void setCartItem(List<CartItem> cartItem) {
		this.cartItem = cartItem;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Product product = (Product) o;
		return name.equals(product.name) && type == product.type;
	}

	@Override public int hashCode() {
		return Objects.hash(name, type);
	}

	@Override public int compareTo(Product that) {
		return Comparator.comparing(Product::getType).thenComparing(Product::getName).compare(this, that);
	}

	@Override public String toString() {
		return "Product{" + "id=" + id + ", name='" + name + '\'' + ", price=" + price + ", type=" + type + '}';
	}
}
