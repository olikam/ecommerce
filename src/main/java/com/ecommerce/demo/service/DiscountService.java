package com.ecommerce.demo.service;

import com.ecommerce.demo.entity.Cart;

public interface DiscountService {

    void apply(Cart cart);
}
