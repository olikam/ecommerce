package com.bestseller.ecommerce.controller;

import com.bestseller.ecommerce.exception.UserAlreadyExistsException;
import com.bestseller.ecommerce.entity.User;
import com.bestseller.ecommerce.util.JwtUtil;
import com.bestseller.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/register")
	public ResponseEntity<String> register(@Valid User user) {
		try {
			userService.register(user);
		} catch (UserAlreadyExistsException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
		return new ResponseEntity<>(JwtUtil.generateToken(user), HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(String username, String password) {
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			User user = (User) authentication.getPrincipal();
			return new ResponseEntity<>(JwtUtil.generateToken(user), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
