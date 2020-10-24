package com.bestseller.ecommerce.repository;

import com.bestseller.ecommerce.entity.Product;
import com.bestseller.ecommerce.model.ProductType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

	Optional<Product> findByNameContainingIgnoreCase(String name);

	Optional<Product> findByIdAndType(Long id, ProductType type);
}
