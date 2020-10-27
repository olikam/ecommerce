package com.bestseller.ecommerce;

import com.bestseller.ecommerce.entity.Product;
import com.bestseller.ecommerce.exception.DuplicateProductException;
import com.bestseller.ecommerce.exception.ProductNotFoundException;
import com.bestseller.ecommerce.model.ProductType;
import com.bestseller.ecommerce.repository.ProductRepository;
import com.bestseller.ecommerce.service.ProductService;
import com.bestseller.ecommerce.service.ProductServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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
public class ProductServiceTest {

	@TestConfiguration
	static class ProductServiceImplTestConfig {

		@Bean
		public ProductService productService() {
			return new ProductServiceImpl();
		}
	}

	@Autowired
	private ProductService productService;

	@MockBean
	private ProductRepository productRepository;

	private static final Product latte = new Product("Latte", new BigDecimal("5.0"), ProductType.DRINK);

	private static final Product blackCoffee = new Product("Black Coffee", new BigDecimal("4.0"), ProductType.DRINK);

	private static final Product milk = new Product("Milk", new BigDecimal("2.0"), ProductType.TOPPING);

	private static final Product choco = new Product("Choco", new BigDecimal("3.0"), ProductType.TOPPING);

	private static final List<Product> products = new ArrayList<>();

	@BeforeAll
	public static void setUp() {
		latte.setId(1L);
		products.add(latte);
		blackCoffee.setId(2L);
		products.add(blackCoffee);
		milk.setId(3L);
		products.add(milk);
		choco.setId(4L);
		products.add(choco);
	}

	@Test
	public void testGetEmpty() {
		assertThat(productService.getAllProducts()).containsExactlyInAnyOrderElementsOf(new ArrayList<>());
	}

	@Test
	public void testGetAll() {
		Mockito.when(productRepository.findAll()).thenReturn(products);
		assertThat(productService.getAllProducts()).containsExactlyInAnyOrderElementsOf(products);
	}

	@Test
	public void testGet() {
		Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(latte));
		assertThat(productService.getProduct(1L)).hasValue(latte);

		Mockito.when(productRepository.findById(2L)).thenReturn(Optional.of(blackCoffee));
		assertThat(productService.getProduct(2L)).hasValue(blackCoffee);

		Mockito.when(productRepository.findById(3L)).thenReturn(Optional.of(milk));
		assertThat(productService.getProduct(3L)).hasValue(milk);

		Mockito.when(productRepository.findById(4L)).thenReturn(Optional.of(choco));
		assertThat(productService.getProduct(4L)).hasValue(choco);
	}

	@Test
	public void testCreateException() {
		Mockito.when(productRepository.findByNameIgnoreCase(latte.getName())).thenReturn(Optional.of(latte));
		Assertions.assertThrows(DuplicateProductException.class, () -> productService.create(latte));
	}

	@Test
	public void testCreate() {
		Mockito.when(productRepository.findByNameIgnoreCase(latte.getName())).thenReturn(Optional.empty());
		Assertions.assertDoesNotThrow(() -> productService.create(latte));
	}

	@Test
	public void testUpdateException() {
		Mockito.when(productRepository.findByNameIgnoreCase(latte.getName())).thenReturn(Optional.empty());
		Assertions.assertThrows(ProductNotFoundException.class, () -> productService.update(latte));
	}

	@Test
	public void testDeleteException() {
		Mockito.when(productRepository.findById(1L)).thenReturn(Optional.empty());
		Assertions.assertDoesNotThrow(() -> productService.delete(1L));
	}

}
