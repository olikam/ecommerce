package com.bestseller.ecommerce.model;

import com.bestseller.ecommerce.entity.Cart;
import com.bestseller.ecommerce.entity.Product;
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
