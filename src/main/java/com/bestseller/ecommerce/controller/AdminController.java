package com.bestseller.ecommerce.controller;

import com.bestseller.ecommerce.entity.Product;
import com.bestseller.ecommerce.model.ProductCreateRequest;
import com.bestseller.ecommerce.model.ProductDeleteRequest;
import com.bestseller.ecommerce.model.ProductUpdateRequest;
import com.bestseller.ecommerce.model.Report;
import com.bestseller.ecommerce.service.ProductService;
import com.bestseller.ecommerce.service.ReportService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/product")
    public ResponseEntity<String> create(@Valid @RequestBody ProductCreateRequest productCreateRequest) {
        Product newProduct = new Product(productCreateRequest.getName(),
                BigDecimal.valueOf(productCreateRequest.getPrice()).setScale(2, RoundingMode.HALF_UP),
                productCreateRequest.getType());
        productService.create(newProduct);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/product")
    public ResponseEntity<String> update(@Valid @RequestBody ProductUpdateRequest productUpdateRequest) {
        productService.update(productUpdateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/product")
    public ResponseEntity<String> delete(@Valid @RequestBody ProductDeleteRequest productDeleteRequest) {
        productService.delete(productDeleteRequest.getProductId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
