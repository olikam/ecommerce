package com.bestseller.ecommerce.service;

import com.bestseller.ecommerce.entity.Product;
import com.bestseller.ecommerce.model.ProductUpdateRequest;

import java.util.List;
import java.util.Optional;

public interface ProductService {

	List<Product> getAllProducts();

	Optional<Product> getProduct(Long id);

	void create(Product product);

	void update(ProductUpdateRequest productUpdateRequest);

	void delete(Long productId);

}
