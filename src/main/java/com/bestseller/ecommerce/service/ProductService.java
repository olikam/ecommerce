package com.bestseller.ecommerce.service;

import com.bestseller.ecommerce.entity.Product;
import com.bestseller.ecommerce.model.ProductType;

import java.util.List;
import java.util.Optional;

public interface ProductService {

	List<Product> getAllProducts();

	Optional<Product> getProduct(Long id, ProductType type);

	void create(Product product);

	void update(Product product);

	void delete(Long productId);

}
