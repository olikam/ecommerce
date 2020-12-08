package com.bestseller.ecommerce.controller;

import com.bestseller.ecommerce.entity.Order;
import com.bestseller.ecommerce.service.OrderService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/order")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getOrders() {
        return new ResponseEntity<>(orderService.getOrders(getUser()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Order> placeOrder() {
        return new ResponseEntity<>(orderService.create(getUser()), HttpStatus.OK);
    }
}
