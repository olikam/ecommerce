package com.ecommerce.demo.service;

import com.ecommerce.demo.entity.Product;
import com.ecommerce.demo.model.ProductUpdateRequest;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> getAllProducts();

    Optional<Product> getProduct(Long id);

    void create(Product product);

    void update(ProductUpdateRequest productUpdateRequest);

    void delete(Long productId);

}
