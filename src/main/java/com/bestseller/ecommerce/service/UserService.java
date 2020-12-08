package com.bestseller.ecommerce.service;

import com.bestseller.ecommerce.entity.User;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void register(User user);

    List<User> getAllUsers();
}
