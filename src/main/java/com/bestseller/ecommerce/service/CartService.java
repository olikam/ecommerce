package com.bestseller.ecommerce.service;

import com.bestseller.ecommerce.exception.ProductNotFoundException;
import com.bestseller.ecommerce.entity.Cart;
import com.bestseller.ecommerce.model.AddItemRequest;
import com.bestseller.ecommerce.model.DeleteItemRequest;
import com.bestseller.ecommerce.entity.User;

public interface CartService {

	Cart getCart(User user);

	Cart add(User user, AddItemRequest addItemRequest);

	Cart delete(User user, DeleteItemRequest deleteItemRequest);

	void empty(User user);
}
