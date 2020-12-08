package com.ecommerce.demo.service;

import com.ecommerce.demo.entity.Cart;
import com.ecommerce.demo.entity.User;
import com.ecommerce.demo.model.AddItemRequest;
import com.ecommerce.demo.model.DeleteItemRequest;

public interface CartService {

    Cart getCart(User user);

    Cart add(User user, AddItemRequest addItemRequest);

    Cart delete(User user, DeleteItemRequest deleteItemRequest);

    void empty(User user);
}
