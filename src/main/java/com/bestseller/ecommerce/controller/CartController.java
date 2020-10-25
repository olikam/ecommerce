package com.bestseller.ecommerce.controller;

import com.bestseller.ecommerce.entity.Cart;
import com.bestseller.ecommerce.entity.Product;
import com.bestseller.ecommerce.model.AddItemRequest;
import com.bestseller.ecommerce.model.DeleteItemRequest;
import com.bestseller.ecommerce.model.ProductResponse;
import com.bestseller.ecommerce.service.CartService;
import com.bestseller.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController extends BaseController {

	@Autowired
	private ProductService productService;

	@Autowired
	private CartService cartService;

	@GetMapping("/products")
	public ResponseEntity<ProductResponse> getProducts() {
		List<Product> allProducts = productService.getAllProducts();
		Cart cart = cartService.getCart(getUser());
		return new ResponseEntity<>(new ProductResponse(allProducts, cart), HttpStatus.OK);
	}

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
