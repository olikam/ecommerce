package com.bestseller.ecommerce.controller;

import com.bestseller.ecommerce.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class BaseController {

	protected User getUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

}
