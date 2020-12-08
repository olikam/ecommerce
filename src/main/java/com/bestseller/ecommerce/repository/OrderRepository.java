package com.bestseller.ecommerce.repository;

import com.bestseller.ecommerce.entity.Order;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findByUsername(String username);
}
