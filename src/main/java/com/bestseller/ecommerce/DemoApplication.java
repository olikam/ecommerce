package com.bestseller.ecommerce;

import com.bestseller.ecommerce.entity.Product;
import com.bestseller.ecommerce.model.ProductType;
import com.bestseller.ecommerce.entity.User;
import com.bestseller.ecommerce.model.UserRole;
import com.bestseller.ecommerce.service.ProductService;
import com.bestseller.ecommerce.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.math.RoundingMode;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

//	@Bean
//	CommandLineRunner runner(ProductService productService, UserService userService) {
//		return args -> {
//			userService.register(new User("Soner", "Sezgin", "+905332108093", "esoner.sezgin@gmail.com", "", "1", UserRole.USER));
//			userService.register(new User("Paul", "Muaddib", "0000000000", "paul.muaddib@arrakis.com", "", "1", UserRole.ADMIN));
//
//			productService.create(new Product("Black Coffee", new BigDecimal("4").setScale(2, RoundingMode.HALF_UP), ProductType.DRINK));
//			productService.create(new Product("Latte", new BigDecimal("5").setScale(2, RoundingMode.HALF_UP), ProductType.DRINK));
//			productService.create(new Product("Mocha", new BigDecimal("6").setScale(2, RoundingMode.HALF_UP), ProductType.DRINK));
//			productService.create(new Product("Tea", new BigDecimal("3").setScale(2, RoundingMode.HALF_UP), ProductType.DRINK));
//
//			productService.create(new Product("Milk", new BigDecimal("2").setScale(2, RoundingMode.HALF_UP), ProductType.TOPPING));
//			productService.create(new Product("Hazelnut syrup", new BigDecimal("3").setScale(2, RoundingMode.HALF_UP), ProductType.TOPPING));
//			productService.create(new Product("Chocolate sauce", new BigDecimal("5").setScale(2, RoundingMode.HALF_UP), ProductType.TOPPING));
//			productService.create(new Product("Lemon", new BigDecimal("2").setScale(2, RoundingMode.HALF_UP), ProductType.TOPPING));
//		};
//	}
}
