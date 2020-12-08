package com.ecommerce.demo;

import com.ecommerce.demo.entity.Product;
import com.ecommerce.demo.entity.User;
import com.ecommerce.demo.model.ProductType;
import com.ecommerce.demo.model.UserRole;
import com.ecommerce.demo.service.ProductService;
import com.ecommerce.demo.service.UserService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    @Profile("!test")
    CommandLineRunner runner(ProductService productService, UserService userService) {
        return args -> {
            userService.register(new User("Soner", "Sezgin", "+905332108093", "esoner.sezgin@gmail.com", UserRole.USER, "", "1"));
            userService.register(new User("Paul", "Muaddib", "0000000000", "paul.muaddib@arrakis.com", UserRole.ADMIN, "", "1"));

            productService.create(new Product("Black Coffee", new BigDecimal("4").setScale(2, RoundingMode.HALF_UP), ProductType.DRINK));
            productService.create(new Product("Latte", new BigDecimal("5").setScale(2, RoundingMode.HALF_UP), ProductType.DRINK));
            productService.create(new Product("Mocha", new BigDecimal("6").setScale(2, RoundingMode.HALF_UP), ProductType.DRINK));
            productService.create(new Product("Tea", new BigDecimal("3").setScale(2, RoundingMode.HALF_UP), ProductType.DRINK));

            productService.create(new Product("Milk", new BigDecimal("2").setScale(2, RoundingMode.HALF_UP), ProductType.TOPPING));
            productService.create(new Product("Hazelnut syrup", new BigDecimal("3").setScale(2, RoundingMode.HALF_UP), ProductType.TOPPING));
            productService.create(new Product("Chocolate sauce", new BigDecimal("5").setScale(2, RoundingMode.HALF_UP), ProductType.TOPPING));
            productService.create(new Product("Lemon", new BigDecimal("2").setScale(2, RoundingMode.HALF_UP), ProductType.TOPPING));
        };
    }
}
