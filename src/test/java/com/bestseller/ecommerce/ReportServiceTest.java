package com.bestseller.ecommerce;

import com.bestseller.ecommerce.entity.Order;
import com.bestseller.ecommerce.entity.OrderProduct;
import com.bestseller.ecommerce.model.Amount;
import com.bestseller.ecommerce.model.OrderStatus;
import com.bestseller.ecommerce.model.Report;
import com.bestseller.ecommerce.repository.*;
import com.bestseller.ecommerce.service.*;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
public class ReportServiceTest {

	@TestConfiguration
	static class ReportServiceTestConfig {

		@Bean
		public ReportService reportService() {
			return new ReportServiceImpl();
		}

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
	private ReportService reportService;

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
	private CartRepository cartRepository;

	@MockBean
	private OrderProductRepository orderProductRepository;

	@MockBean
	private CartItemRepository cartItemRepository;

	@MockBean
	private ProductRepository productRepository;

	private static List<Order> orders;

	private static String username1 = "esoner.sezgin@gmail.com";

	private static String username2 = "paul.muaddib@arrakis.com";

	private static String username3 = "turin.turambar@gurthang.com";

	private static BigDecimal amount1 = new BigDecimal("155");

	private static BigDecimal amount2 = new BigDecimal("255");

	private static BigDecimal amount3 = new BigDecimal("103");

	private static BigDecimal amount4 = new BigDecimal("172");

	private static BigDecimal discountedAmount1 = new BigDecimal("105");

	private static BigDecimal discountedAmount2 = new BigDecimal("207");

	private static BigDecimal discountedAmount3 = new BigDecimal("72");

	private static BigDecimal discountedAmount4 = new BigDecimal("144");

	private static Map<String, Amount> amountPerCustomer = new LinkedHashMap<>();

	private static Map<String, Integer> mostUsedToppings = new LinkedHashMap<>();

	private static Report report;

	@BeforeAll
	public static void setUp() {
		createOrders();
		createReport();
	}

	private static void createReport() {
		Amount a1 = new Amount();
		a1.addToTotal(amount1);
		a1.addToDiscounted(discountedAmount1);
		amountPerCustomer.put(username1, a1);

		Amount a2 = new Amount();
		a2.addToTotal(amount2);
		a2.addToDiscounted(discountedAmount2);
		amountPerCustomer.put(username2, a2);

		Amount a3 = new Amount();
		a3.addToTotal(amount3);
		a3.addToTotal(amount4);
		a3.addToDiscounted(discountedAmount3);
		a3.addToDiscounted(discountedAmount4);
		amountPerCustomer.put(username3, a3);

		mostUsedToppings.put("choco", 28);
		mostUsedToppings.put("honey", 20);
		mostUsedToppings.put("milk", 12);

		report = new Report(amountPerCustomer, mostUsedToppings);
	}

	private static void createOrders() {
		Order o1 = new Order();
		o1.setId(1L);
		o1.setUsername(username1);
		o1.setTotalAmount(amount1);
		o1.setDiscountedAmount(discountedAmount1);
		o1.setOrderProducts(createOrderProducts());
		o1.setStatus(OrderStatus.PREPARING);
		o1.setDateCreated(LocalDateTime.now());

		Order o2 = new Order();
		o2.setId(2L);
		o2.setUsername(username2);
		o2.setTotalAmount(amount2);
		o2.setDiscountedAmount(discountedAmount2);
		o2.setOrderProducts(createOrderProducts());
		o2.setStatus(OrderStatus.PREPARING);
		o2.setDateCreated(LocalDateTime.now());

		Order o3 = new Order();
		o3.setId(3L);
		o3.setUsername(username3);
		o3.setTotalAmount(amount3);
		o3.setDiscountedAmount(discountedAmount3);
		o3.setOrderProducts(createOrderProducts());
		o3.setStatus(OrderStatus.PREPARING);
		o3.setDateCreated(LocalDateTime.now());

		Order o4 = new Order();
		o4.setId(3L);
		o4.setUsername(username3);
		o4.setTotalAmount(amount4);
		o4.setOrderProducts(createOrderProducts());
		o4.setDiscountedAmount(discountedAmount4);
		o4.setStatus(OrderStatus.PREPARING);
		o4.setDateCreated(LocalDateTime.now());

		orders = List.of(o1, o2, o3, o4);
	}

	private static List<OrderProduct> createOrderProducts() {
		OrderProduct op1 = new OrderProduct();
		op1.setPrice(new BigDecimal("70"));
		op1.setQuantity(3);
		op1.setDrinkName("latte");
		op1.setToppingsNames("choco,milk,honey");

		OrderProduct op2 = new OrderProduct();
		op2.setPrice(new BigDecimal("35"));
		op2.setQuantity(2);
		op2.setDrinkName("black coffee");
		op2.setToppingsNames("honey");

		OrderProduct op3 = new OrderProduct();
		op3.setPrice(new BigDecimal("50"));
		op3.setQuantity(4);
		op3.setDrinkName("latte");
		op3.setToppingsNames("choco");

		return List.of(op1, op2, op3);
	}

	@Test
	public void testGenerateReport() {
		Mockito.when(orderRepository.findAll()).thenReturn(orders);
		Report actual = reportService.generateReport();
		Condition<Report> condition = new Condition<>(
				r -> {
					if(r.getAmountPerCustomer().size() != report.getAmountPerCustomer().size()) {
						return false;
					}
					if(r.getMostUsedToppings().size() != report.getMostUsedToppings().size()) {
						return false;
					}

					boolean falseResult = r.getMostUsedToppings().entrySet()
							.stream()
							.anyMatch(e -> !e.getValue().equals(report.getMostUsedToppings().get(e.getKey())));
					if(falseResult) {
						return false;
					}

					falseResult = r.getAmountPerCustomer().entrySet()
							.stream()
							.anyMatch(e -> !e.getValue().equals(report.getAmountPerCustomer().get(e.getKey())));
					if(falseResult) {
						return false;
					}

					return true;
				}, "condition");
		assertThat(actual).is(condition);
	}
}
