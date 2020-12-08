package com.ecommerce.demo.repository;

import com.ecommerce.demo.entity.Product;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    Optional<Product> findByNameIgnoreCase(String name);
}
