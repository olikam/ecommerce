package com.bestseller.ecommerce.controller;

import com.bestseller.ecommerce.entity.Cart;
import com.bestseller.ecommerce.entity.Product;
import com.bestseller.ecommerce.model.ProductResponse;
import com.bestseller.ecommerce.service.CartService;
import com.bestseller.ecommerce.service.ProductService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController extends BaseController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<ProductResponse> getProducts() {
        List<Product> allProducts = productService.getAllProducts();
        Cart cart = cartService.getCart(getUser());
        return new ResponseEntity<>(new ProductResponse(allProducts, cart), HttpStatus.OK);
    }
}
