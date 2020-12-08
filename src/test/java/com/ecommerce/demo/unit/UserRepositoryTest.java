package com.ecommerce.demo.unit;

import static org.assertj.core.api.Assertions.assertThat;

import com.ecommerce.demo.entity.User;
import com.ecommerce.demo.model.UserRole;
import com.ecommerce.demo.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    private User user = new User("soner", "sezgin", "+905332108093", "esoner.sezgin@gmail.com", UserRole.USER, "", "1");

    @BeforeEach
    public void setUp() {
        testEntityManager.persist(user);
        testEntityManager.flush();
    }

    @Test
    public void testFindByUsername() {
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        assertThat(optionalUser).isNotEmpty();
        assertThat(optionalUser.get().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    public void testFindByPhoneNumber() {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(user.getPhoneNumber());
        assertThat(optionalUser).isNotEmpty();
        assertThat(optionalUser.get().getPhoneNumber()).isEqualTo(user.getPhoneNumber());
    }
}
