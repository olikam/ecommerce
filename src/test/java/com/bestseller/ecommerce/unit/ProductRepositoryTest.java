package com.bestseller.ecommerce.unit;

import static org.assertj.core.api.Assertions.assertThat;

import com.bestseller.ecommerce.entity.Product;
import com.bestseller.ecommerce.model.ProductType;
import com.bestseller.ecommerce.repository.ProductRepository;
import java.math.BigDecimal;
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
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ProductRepository productRepository;

    private Product drink = new Product("Latte", new BigDecimal("5.0"), ProductType.DRINK);

    private Product topping = new Product("Milk", new BigDecimal("2.0"), ProductType.TOPPING);

    @BeforeEach
    public void setUp() {
        Long drinkId = (Long) testEntityManager.persistAndGetId(drink);
        drink.setId(drinkId);
        Long toppingId = (Long) testEntityManager.persistAndGetId(topping);
        topping.setId(toppingId);
    }

    @Test
    public void testFindByNameIgnoreCase() {
        Optional<Product> optionalDrink = productRepository.findByNameIgnoreCase("latte");
        assertThat(optionalDrink).isNotEmpty();
        assertThat(optionalDrink.get().getName()).isEqualTo(drink.getName());

        Optional<Product> optionalTopping = productRepository.findByNameIgnoreCase("milk");
        assertThat(optionalTopping).isNotEmpty();
        assertThat(optionalTopping.get().getName()).isEqualTo(topping.getName());
    }

    @Test
    public void testFindById() {
        Optional<Product> optionalDrink = productRepository.findById(drink.getId());
        assertThat(optionalDrink).hasValue(drink);

        Optional<Product> optionalTopping = productRepository.findById(topping.getId());
        assertThat(optionalTopping).hasValue(topping);
    }

    @Test
    public void testDelete() {
        productRepository.delete(drink);
        Optional<Product> optionalDrink = productRepository.findById(drink.getId());
        assertThat(optionalDrink).isEmpty();
    }

}
