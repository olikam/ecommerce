package com.bestseller.ecommerce.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.bestseller.ecommerce.entity.User;
import com.bestseller.ecommerce.exception.UserAlreadyExistsException;
import com.bestseller.ecommerce.model.UserRole;
import com.bestseller.ecommerce.repository.UserRepository;
import com.bestseller.ecommerce.service.UserService;
import com.bestseller.ecommerce.service.UserServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    private User user = new User("soner", "sezgin", "+905332108093", "esoner.sezgin@gmail.com", UserRole.USER, "", "1");

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

    @Test
    public void testGet() {
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
        assertThat(userDetails.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    public void testGetAll() {
        List<User> users = List.of(this.user);
        Mockito.when(userRepository.findAll()).thenReturn(users);
        assertThat(userService.getAllUsers()).containsExactlyInAnyOrderElementsOf(users);
    }

    @TestConfiguration
    static class UserServiceImplTestConfig {

        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
    }
}
