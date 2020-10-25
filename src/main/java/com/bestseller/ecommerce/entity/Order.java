package com.bestseller.ecommerce.entity;

import com.bestseller.ecommerce.model.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime dateCreated;

	private OrderStatus status;

	private BigDecimal totalAmount;

	private BigDecimal discountedAmount;

	@OneToMany
	private List<OrderProduct> orderProducts = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
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

	public List<OrderProduct> getOrderProducts() {
		return orderProducts;
	}

	public void setOrderProducts(List<OrderProduct> orderProducts) {
		this.orderProducts = orderProducts;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Order order = (Order) o;
		return Objects.equals(id, order.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
