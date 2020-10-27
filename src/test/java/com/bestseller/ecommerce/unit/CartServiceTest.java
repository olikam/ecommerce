package com.bestseller.ecommerce.unit;

import com.bestseller.ecommerce.entity.Cart;
import com.bestseller.ecommerce.entity.CartItem;
import com.bestseller.ecommerce.entity.Product;
import com.bestseller.ecommerce.entity.User;
import com.bestseller.ecommerce.model.AddItemRequest;
import com.bestseller.ecommerce.model.DeleteItemRequest;
import com.bestseller.ecommerce.model.ProductType;
import com.bestseller.ecommerce.model.UserRole;
import com.bestseller.ecommerce.repository.CartItemRepository;
import com.bestseller.ecommerce.repository.CartRepository;
import com.bestseller.ecommerce.repository.ProductRepository;
import com.bestseller.ecommerce.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CartServiceTest {

	@TestConfiguration
	static class CartServiceImplTestConfig {

		@Bean
		public CartService cartService() {
			return new CartServiceImpl();
		}

		@Bean
		public ProductService productService() {
			return new ProductServiceImpl();
		}

		@Bean
		public DiscountService discountService() {
			return new DiscountServiceImpl();
		}
	}

	@Autowired
	private CartService cartService;

	@Autowired
	private ProductService productService;

	@Autowired
	private DiscountService discountService;

	@MockBean
	private CartRepository cartRepository;

	@MockBean
	private CartItemRepository cartItemRepository;

	@MockBean
	private ProductRepository productRepository;

	private Cart cart = new Cart();

	private User user = new User("soner", "sezgin", "+905332108093", "esoner.sezgin@gmail.com", UserRole.USER, "", "1");

	@BeforeEach
	public void setUp() {
		user.setId(1L);
		cart = new Cart();
		cart.setUser(user);
	}

	@Test
	public void testGet() {
		Mockito.when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
		Cart cart = cartService.getCart(user);
		assertThat(cart).isEqualTo(cart);
	}

	@Test
	public void testCreateNewCart() {
		Mockito.when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());
		Mockito.when(cartRepository.save(cart)).thenReturn(cart);
		assertThat(cartService.getCart(user)).isEqualTo(cart);
	}

	@Test
	public void testCartAmount() {
		Product latte = new Product("Latte", new BigDecimal("5.0"), ProductType.DRINK);
		Product choco = new Product("Choco", new BigDecimal("3.0"), ProductType.TOPPING);
		Product blackCoffee = new Product("Black Coffee", new BigDecimal("4.0"), ProductType.DRINK);
		List<Product> products1 = new ArrayList<>();
		products1.add(latte);
		products1.add(choco);
		CartItem item1 = new CartItem();
		item1.setProducts(products1, 3);

		List<Product> products2 = new ArrayList<>();
		products2.add(blackCoffee);
		CartItem item2 = new CartItem();
		item2.setProducts(products2, 2);

		cart.addCartItem(item1);
		cart.addCartItem(item2);
		discountService.apply(cart);

		Mockito.when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
		Cart actualCart = cartService.getCart(user);

		assertThat(actualCart.getTotalAmount()).isEqualByComparingTo(cart.getTotalAmount());
		assertThat(actualCart.getDiscountedAmount()).isEqualByComparingTo(cart.getDiscountedAmount());
	}

	@Test
	public void testAdd() {
		// Create products
		Product latte = new Product("Latte", new BigDecimal("5.0"), ProductType.DRINK);
		latte.setId(1L);
		Product milk = new Product("Milk", new BigDecimal("2.0"), ProductType.TOPPING);
		milk.setId(2L);
		Product choco = new Product("Choco", new BigDecimal("3.0"), ProductType.TOPPING);
		choco.setId(3L);
		List<Product> products = new ArrayList<>();
		products.add(latte);
		products.add(milk);
		products.add(choco);

		// Create reference cartItem using products above
		CartItem cartItem = new CartItem();
		cartItem.setProducts(products, 3);

		// Create add addItemRequest so as to create exactly same as the cartItem above
		AddItemRequest addItemRequest = new AddItemRequest();
		addItemRequest.setDrinkId(latte.getId());
		List<Long> toppingIds = new ArrayList<>();
		toppingIds.add(milk.getId());
		toppingIds.add(choco.getId());
		addItemRequest.setToppingIds(toppingIds);
		int quantity = 3;
		addItemRequest.setQuantity(quantity);

		Mockito.when(productRepository.findById(latte.getId())).thenReturn(Optional.of(latte));
		Mockito.when(productRepository.findById(milk.getId())).thenReturn(Optional.of(milk));
		Mockito.when(productRepository.findById(choco.getId())).thenReturn(Optional.of(choco));
		Mockito.when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
		Mockito.when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
		Mockito.when(cartRepository.save(cart)).thenReturn(cart);
		Cart actualCart = cartService.add(user, addItemRequest);
		assertThat(actualCart).isEqualTo(cart);

	}

	@Test
	public void testDeleteOneItem() {
		int cartItemQuantity = 2;
		addCartItemToCart(cartItemQuantity);
		DeleteItemRequest deleteItemRequest = new DeleteItemRequest();
		deleteItemRequest.setCartItemId(1L);
		int deletedQuantity = 1;
		deleteItemRequest.setQuantity(deletedQuantity);

		Mockito.when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
		Cart actualCart = cartService.delete(user, deleteItemRequest);
		int expectedItemSize = Math.max(cartItemQuantity - deletedQuantity, 0);
		assertThat(actualCart.getCartItems().size()).isEqualTo(expectedItemSize);
	}

	@Test
	public void testMoreThanItemSize() {
		int cartItemQuantity = 2;
		addCartItemToCart(cartItemQuantity);
		DeleteItemRequest deleteItemRequest = new DeleteItemRequest();
		deleteItemRequest.setCartItemId(1L);
		int deletedQuantity = 4;
		deleteItemRequest.setQuantity(deletedQuantity);

		Mockito.when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
		Cart actualCart = cartService.delete(user, deleteItemRequest);
		int expectedItemSize = Math.max(cartItemQuantity - deletedQuantity, 0);
		assertThat(actualCart.getCartItems().size()).isEqualTo(expectedItemSize);
	}

	@Test
	public void testDeleteAllItems() {
		addCartItemToCart(3);
		DeleteItemRequest deleteItemRequest = new DeleteItemRequest();
		deleteItemRequest.setCartItemId(1L);
		deleteItemRequest.setQuantity(3);

		Mockito.when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
		Cart actualCart = cartService.delete(user, deleteItemRequest);
		int expectedItemSize = Math.max(cart.getCartItems().size() - deleteItemRequest.getQuantity(), 0);
		assertThat(actualCart.getCartItems().size()).isEqualTo(expectedItemSize);
	}

	@Test
	public void testEmpty() {
		addCartItemToCart(5);
		Mockito.when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
		cartService.empty(user);
		Mockito.when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.empty());

		Cart newCart = new Cart();
		newCart.setUser(user);
		Mockito.when(cartRepository.save(newCart)).thenReturn(newCart);

		Cart cart = cartService.getCart(user);
		assertThat(cart.getCartItems()).isEmpty();

	}

	private void addCartItemToCart(int quantity) {
		Product latte = new Product("Latte", new BigDecimal("5.0"), ProductType.DRINK);
		latte.setId(1L);
		Product milk = new Product("Milk", new BigDecimal("2.0"), ProductType.TOPPING);
		milk.setId(2L);
		Product choco = new Product("Choco", new BigDecimal("3.0"), ProductType.TOPPING);
		choco.setId(3L);
		List<Product> products = new ArrayList<>();
		products.add(latte);
		products.add(milk);
		products.add(choco);

		CartItem cartItem = new CartItem();
		cartItem.setProducts(products, quantity);
		cartItem.setId(1L);
		cart.addCartItem(cartItem);
	}

}
