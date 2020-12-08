package com.ecommerce.demo.service;

import com.ecommerce.demo.entity.User;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void register(User user);

    List<User> getAllUsers();
}
