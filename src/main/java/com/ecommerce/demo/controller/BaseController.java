package com.ecommerce.demo.controller;

import com.ecommerce.demo.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class BaseController {

    protected User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
