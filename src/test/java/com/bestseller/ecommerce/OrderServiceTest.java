package com.bestseller.ecommerce;

import com.bestseller.ecommerce.entity.Order;
import com.bestseller.ecommerce.entity.User;
import com.bestseller.ecommerce.model.UserRole;
import com.bestseller.ecommerce.repository.*;
import com.bestseller.ecommerce.service.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
public class OrderServiceTest {

	@TestConfiguration
	static class OrderServiceImplTestConfig {

		@Bean
		public OrderService orderService() {
			return new OrderServiceImpl();
		}

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
	private OrderService orderService;

	@Autowired
	private CartService cartService;

	@Autowired
	private ProductService productService;

	@Autowired
	private DiscountService discountService;

	@MockBean
	private OrderRepository orderRepository;

	@MockBean
	private OrderProductRepository orderProductRepository;

	@MockBean
	private CartRepository cartRepository;

	@MockBean
	private CartItemRepository cartItemRepository;

	@MockBean
	private ProductRepository productRepository;

	private static final User user = new User("soner", "sezgin", "+905332108093", "esoner.sezgin@gmail.com", "", "1", UserRole.USER);

	@BeforeAll
	public static void setUp() {
		user.setId(1L);
	}

	@Test
	public void testGetOrders() {
		Order order1 = new Order();
		order1.setUsername(user.getUsername());
		Order order2 = new Order();
		order2.setUsername(user.getUsername());

		List<Order> expected = List.of(order1, order2);
		Mockito.when(orderRepository.findByUsername(user.getUsername())).thenReturn(expected);
		List<Order> actual = orderService.getOrders(user);
		assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
	}

	@Test
	public void testGetAllOrders() {
		Order order1 = new Order();
		order1.setUsername(user.getUsername());
		Order order2 = new Order();
		order2.setUsername(user.getUsername());

		Order order3 = new Order();
		User user2 = new User("paul", "muaddib", "+905332108093", "paul.muaddib@arrakis.com", "", "1", UserRole.ADMIN);
		order3.setUsername(user2.getUsername());

		Order order4 = new Order();
		User user3 = new User("turin", "turambar", "+905332108093", "turin.turambar@gurthang.com", "", "1", UserRole.ADMIN);
		order4.setUsername(user3.getUsername());

		List<Order> expected = List.of(order1, order2, order3, order4);
		Mockito.when(orderRepository.findAll()).thenReturn(expected);
		List<Order> actual = orderService.getAllOrders();
		assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
	}

}
