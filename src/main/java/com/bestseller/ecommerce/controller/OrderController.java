package com.bestseller.ecommerce.controller;

import com.bestseller.ecommerce.entity.Order;
import com.bestseller.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
