package com.ecommerce.demo.controller;

import com.ecommerce.demo.entity.Cart;
import com.ecommerce.demo.model.AddItemRequest;
import com.ecommerce.demo.model.DeleteItemRequest;
import com.ecommerce.demo.service.CartService;
import java.util.Objects;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController extends BaseController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<Cart> getCart() {
        return new ResponseEntity<>(cartService.getCart(getUser()), HttpStatus.OK);
    }

    @PostMapping("/item")
    public ResponseEntity<Cart> addItem(@Valid @RequestBody AddItemRequest addItemRequest) {
        return new ResponseEntity<>(cartService.add(getUser(), addItemRequest), HttpStatus.OK);
    }

    @DeleteMapping("/item/{id}")
    public ResponseEntity<Cart> deleteItem(@PathVariable Long id, @RequestParam(value = "quantity", required = false) Integer quantity) {
        DeleteItemRequest deleteItemRequest = new DeleteItemRequest(id, Objects.requireNonNullElse(quantity, 1));
        return new ResponseEntity<>(cartService.delete(getUser(), deleteItemRequest), HttpStatus.OK);
    }

    @DeleteMapping("/items")
    public ResponseEntity<String> emptyCart() {
        cartService.empty(getUser());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
