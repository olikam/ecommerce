package com.ecommerce.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import org.apache.commons.collections4.CollectionUtils;

@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Product> products = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
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

    public void setProducts(List<Product> products, int quantity) {
        this.products = products;
        this.quantity = quantity;
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
        if (quantity == 0) {
            products.clear();
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

    public void setProducts(List<Product> products) {
        this.products = products;
        increaseQuantityBy(1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CartItem cartItem = (CartItem) o;
        if (products == null && cartItem.getProducts() == null) {
            return true;
        } else if (products == null || cartItem.getProducts() == null) {
            return false;
        }
        return CollectionUtils.isEqualCollection(products, cartItem.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(products);
    }

    @Override
    public String toString() {
        return "CartItem{" + "id=" + id + ", products=" + products + ", cart=" + cart + ", quantity=" + quantity + '}';
    }
}
