package com.bestseller.ecommerce.service;

import com.bestseller.ecommerce.entity.User;
import com.bestseller.ecommerce.exception.UserAlreadyExistsException;
import com.bestseller.ecommerce.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	/**
	 * Registers the specified user.
	 *
	 * @param user
	 * 			  {@link User} to be registered.
	 */
	@Override
	public void register(User user) {
		userRepository.findByUsername(user.getUsername()).ifPresent(u -> {
			throw new UserAlreadyExistsException(u.getUsername());
		});
		userRepository.findByPhoneNumber(user.getPhoneNumber()).ifPresent(u -> {
			throw new UserAlreadyExistsException(u.getPhoneNumber());
		});
		userRepository.save(user);
		logger.info("User created successfully: " + user);
	}

	/**
	 *
	 * @return Returns all users.
	 */
	@Override
	public List<User> getAllUsers() {
		return StreamSupport.stream(userRepository.findAll().spliterator(), false).collect(Collectors.toList());
	}

	/**
	 *
	 * @param username
	 *            E-mail address of the user.
	 * @return Returns {@link UserDetails}
	 * @throws UsernameNotFoundException throws {@link UsernameNotFoundException}
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("User %s cannot be found.", username)));
	}
}
