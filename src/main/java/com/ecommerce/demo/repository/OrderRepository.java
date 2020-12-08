package com.ecommerce.demo.repository;

import com.ecommerce.demo.entity.Order;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findByUsername(String username);
}
