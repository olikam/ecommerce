package com.bestseller.ecommerce;

import com.bestseller.ecommerce.entity.Product;
import com.bestseller.ecommerce.model.ProductType;
import com.bestseller.ecommerce.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.*;
import java.math.BigDecimal;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryIntgTest {

	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private ProductRepository productRepository;

	private Product drink = new Product("Latte", new BigDecimal("5.0"), ProductType.DRINK);

	private Product topping = new Product("Milk", new BigDecimal("2.0"), ProductType.TOPPING);

	@Before
	public void saveProduct() {
		testEntityManager.persist(drink);
		testEntityManager.persist(topping);
	}

	@Test
	public void testFindByNameContainingIgnoreCase() {
		Optional<Product> optionalDrink = productRepository.findByNameContainingIgnoreCase("latte");
		assertThat(optionalDrink).isNotEmpty();
		assertThat(optionalDrink.get().getName()).isEqualTo(drink.getName());

		Optional<Product> optionalTopping = productRepository.findByNameContainingIgnoreCase("milk");
		assertThat(optionalTopping).isNotEmpty();
		assertThat(optionalTopping.get().getName()).isEqualTo(topping.getName());
	}

}
