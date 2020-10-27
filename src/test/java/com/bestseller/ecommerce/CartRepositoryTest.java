package com.bestseller.ecommerce;

import com.bestseller.ecommerce.entity.Cart;
import com.bestseller.ecommerce.entity.CartItem;
import com.bestseller.ecommerce.entity.Product;
import com.bestseller.ecommerce.entity.User;
import com.bestseller.ecommerce.model.ProductType;
import com.bestseller.ecommerce.model.UserRole;
import com.bestseller.ecommerce.repository.CartItemRepository;
import com.bestseller.ecommerce.repository.CartRepository;
import com.bestseller.ecommerce.repository.ProductRepository;
import com.bestseller.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

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
