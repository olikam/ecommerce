package com.bestseller.ecommerce;

import com.bestseller.ecommerce.entity.User;
import com.bestseller.ecommerce.exception.UserAlreadyExistsException;
import com.bestseller.ecommerce.model.UserRole;
import com.bestseller.ecommerce.repository.UserRepository;
import com.bestseller.ecommerce.service.UserService;
import com.bestseller.ecommerce.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
public class UserServiceIntgTest {

	@TestConfiguration
	static class EmployeeServiceImplTestContextConfiguration {

		@Bean
		public UserService userService() {
			return new UserServiceImpl();
		}
	}

	@Autowired
	private UserService userService;

	@MockBean
	private UserRepository userRepository;

	private User user = new User("soner", "sezgin", "+905332108093", "esoner.sezgin@gmail.com", "", "1", UserRole.USER);

	@Test
	public void testRegister() {
		assertDoesNotThrow(() -> userService.register(user));
	}

	@Test
	public void testRegisterSameUsername() {
		Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
		assertThrows(UserAlreadyExistsException.class, () -> userService.register(user));
	}

	@Test
	public void testRegisterSamePhoneNumber() {
		Mockito.when(userRepository.findByPhoneNumber(user.getPhoneNumber())).thenReturn(Optional.of(user));
		assertThrows(UserAlreadyExistsException.class, () -> userService.register(user));
	}

}
