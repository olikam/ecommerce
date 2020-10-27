package com.bestseller.ecommerce.controller;

import com.bestseller.ecommerce.entity.Product;
import com.bestseller.ecommerce.model.DeleteProductRequest;
import com.bestseller.ecommerce.model.ProductCreateUpdateRequest;
import com.bestseller.ecommerce.model.Report;
import com.bestseller.ecommerce.service.ProductService;
import com.bestseller.ecommerce.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ReportService reportService;

	@GetMapping("/report")
	public ResponseEntity<Report> getReport() {
		return new ResponseEntity<>(reportService.generateReport(), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<String> create(@Valid @RequestBody ProductCreateUpdateRequest productCreateUpdateRequest) {
		productService.create(createProduct(productCreateUpdateRequest));
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<String> update(@Valid @RequestBody ProductCreateUpdateRequest productCreateUpdateRequest) {
		productService.update(createProduct(productCreateUpdateRequest));
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity<String> delete(@Valid @RequestBody DeleteProductRequest deleteProductRequest) {
		productService.delete(deleteProductRequest.getProductId());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private Product createProduct(ProductCreateUpdateRequest productCreateUpdateRequest) {
		return new Product(productCreateUpdateRequest.getName(), BigDecimal.valueOf(productCreateUpdateRequest.getPrice()).setScale(2, RoundingMode.HALF_UP), productCreateUpdateRequest
				.getType());
	}
}
