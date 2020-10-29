package com.bestseller.ecommerce.config;

import com.bestseller.ecommerce.exception.DuplicateProductException;
import com.bestseller.ecommerce.exception.ProductNotFoundException;
import com.bestseller.ecommerce.exception.UserAlreadyExistsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@ControllerAdvice
public class DefaultExceptionHandler {

	private static final Logger logger = LogManager.getLogger(DefaultExceptionHandler.class);

	@ExceptionHandler(value = BadCredentialsException.class)
	public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException e) {
		logger.error(e.getMessage());
		return new ResponseEntity<>("Username or password is wrong.", HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(value = ResponseStatusException.class)
	public ResponseEntity<Object> handleAuthenticationException(ResponseStatusException e) {
		logger.error(e.getMessage());
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = ProductNotFoundException.class)
	public ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException e) {
		logger.error(e.getMessage());
		return new ResponseEntity<>("Product not found with the id: " + e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = DuplicateProductException.class)
	public ResponseEntity<Object> handleDuplicateProductException(DuplicateProductException e) {
		logger.error(e.getMessage());
		return new ResponseEntity<>("There is already a product with the name: " + e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = UserAlreadyExistsException.class)
	public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
		logger.error(e.getMessage());
		return new ResponseEntity<>("This user is already registered: " + e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		logger.error(e.getMessage());
		String message = e.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> handleInvalidFormatException(HttpMessageNotReadableException e) {
		logger.error(e.getMessage());
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Object> handleException(Exception e) {
		logger.error(e.getMessage());
		return new ResponseEntity<>("There is an unexpected error.", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}