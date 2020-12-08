package com.ecommerce.demo.unit;

import static org.assertj.core.api.Assertions.assertThat;

import com.ecommerce.demo.entity.Cart;
import com.ecommerce.demo.entity.CartItem;
import com.ecommerce.demo.entity.Product;
import com.ecommerce.demo.entity.User;
import com.ecommerce.demo.model.ProductType;
import com.ecommerce.demo.model.UserRole;
import com.ecommerce.demo.repository.CartItemRepository;
import com.ecommerce.demo.repository.CartRepository;
import com.ecommerce.demo.repository.ProductRepository;
import com.ecommerce.demo.repository.UserRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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
public class CartRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    private List<Product> products;

    @BeforeEach
    public void setUp() {
        user = new User("soner", "sezgin", "+905332108093", "esoner.sezgin@gmail.com", UserRole.USER, "", "1");
        testEntityManager.persistAndFlush(user);
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        assertThat(optionalUser).isNotEmpty();
        user.setId(optionalUser.get().getId());

        Product latte = new Product("Latte", new BigDecimal("5.0"), ProductType.DRINK);
        testEntityManager.persist(latte);
        Product milk = new Product("Milk", new BigDecimal("2.0"), ProductType.TOPPING);
        testEntityManager.persist(milk);
        Product choco = new Product("Choco", new BigDecimal("3.0"), ProductType.TOPPING);
        testEntityManager.persistAndFlush(choco);
        List<Product> products = StreamSupport.stream(productRepository.findAll().spliterator(), false).collect(Collectors.toList());
        assertThat(products).isNotEmpty();
        this.products = products;
    }

    @Test
    public void testFind() {
        CartItem cartItem = persistCartItems();
        List<CartItem> cartItems = StreamSupport.stream(cartItemRepository.findAll().spliterator(), false).collect(Collectors.toList());
        assertThat(cartItems).isNotEmpty().contains(cartItem);

        Cart cart = new Cart();
        cart.setUser(user);
        cartItems.forEach(item -> cart.addCartItem(cartItem));
        testEntityManager.persistAndFlush(cart);

        Optional<Cart> optionalCart = cartRepository.findByUserId(user.getId());
        assertThat(optionalCart).isNotEmpty();
        assertThat(optionalCart.get().getUser().getId()).isEqualTo(user.getId());
        assertThat(optionalCart.get().getCartItems()).isNotEmpty().contains(cartItem);
        assertThat(optionalCart.get().getCartItems().iterator().next().getQuantity()).isEqualTo(2);
    }

    private CartItem persistCartItems() {
        CartItem cartItem = new CartItem();
        cartItem.setProducts(products, 2);
        testEntityManager.persistAndFlush(cartItem);
        return cartItem;
    }
}
