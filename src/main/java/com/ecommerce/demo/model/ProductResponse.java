package com.ecommerce.demo.model;

import com.ecommerce.demo.entity.Cart;
import com.ecommerce.demo.entity.Product;
import java.util.List;

public class ProductResponse {

    private List<Product> products;

    private Cart cart;

    public ProductResponse(List<Product> products, Cart cart) {
        this.products = products;
        this.cart = cart;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
