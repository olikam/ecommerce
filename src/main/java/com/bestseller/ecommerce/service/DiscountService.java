package com.bestseller.ecommerce.service;

import com.bestseller.ecommerce.entity.Cart;

public interface DiscountService {

    void apply(Cart cart);
}
