package com.bestseller.ecommerce.repository;

import com.bestseller.ecommerce.entity.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long>  {

	List<Order> findByUsername(String username);
}
