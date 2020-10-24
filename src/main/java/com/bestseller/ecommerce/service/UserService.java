package com.bestseller.ecommerce.service;

import com.bestseller.ecommerce.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

	void register(User user);

	List<User> getAllUsers();
}
