package com.ecommerce.demo.repository;

import com.ecommerce.demo.entity.Cart;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends CrudRepository<Cart, Long> {

    Optional<Cart> findByUserId(long id);
}
