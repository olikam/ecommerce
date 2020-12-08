package com.ecommerce.demo.service;

import com.ecommerce.demo.entity.Order;
import com.ecommerce.demo.entity.User;
import java.util.List;

public interface OrderService {

    List<Order> getOrders(User user);

    List<Order> getAllOrders();

    Order create(User user);
}
