package com.bestseller.ecommerce.controller;

import com.bestseller.ecommerce.entity.Cart;
import com.bestseller.ecommerce.model.AddItemRequest;
import com.bestseller.ecommerce.model.DeleteItemRequest;
import com.bestseller.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController extends BaseController {

	@Autowired
	private CartService cartService;

	@GetMapping
	public ResponseEntity<Cart> getCart() {
		return new ResponseEntity<>(cartService.getCart(getUser()), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Cart> addItem(@Valid @RequestBody AddItemRequest addItemRequest) {
		return new ResponseEntity<>(cartService.add(getUser(), addItemRequest), HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity<Cart> deleteItem(@Valid @RequestBody DeleteItemRequest deleteItemRequest) {
		return new ResponseEntity<>(cartService.delete(getUser(), deleteItemRequest), HttpStatus.OK);
	}

	@DeleteMapping("/all")
	public ResponseEntity<String> emptyCart() {
		cartService.empty(getUser());
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
