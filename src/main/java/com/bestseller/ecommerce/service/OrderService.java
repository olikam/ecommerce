package com.bestseller.ecommerce.service;

import com.bestseller.ecommerce.entity.Order;
import com.bestseller.ecommerce.entity.User;
import java.util.List;

public interface OrderService {

    List<Order> getOrders(User user);

    List<Order> getAllOrders();

    Order create(User user);
}
