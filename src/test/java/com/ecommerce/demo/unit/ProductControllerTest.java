package com.ecommerce.demo.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ecommerce.demo.config.AuthExceptionHandler;
import com.ecommerce.demo.controller.ProductController;
import com.ecommerce.demo.entity.Cart;
import com.ecommerce.demo.entity.Product;
import com.ecommerce.demo.entity.User;
import com.ecommerce.demo.model.ProductResponse;
import com.ecommerce.demo.model.ProductType;
import com.ecommerce.demo.model.UserRole;
import com.ecommerce.demo.repository.CartRepository;
import com.ecommerce.demo.service.CartService;
import com.ecommerce.demo.service.ProductService;
import com.ecommerce.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
@WithMockUser
@ActiveProfiles("test")
public class ProductControllerTest {

    private static final String product1Name = "Latte";
    private static final String product2Name = "Milk";
    private static final BigDecimal product1Price = new BigDecimal("5.0").setScale(2, RoundingMode.HALF_UP);
    private static final BigDecimal product2Price = new BigDecimal("2.0").setScale(2, RoundingMode.HALF_UP);
    private static final ProductType product1Type = ProductType.DRINK;
    private static final ProductType product2Type = ProductType.TOPPING;
    private static final User user = new User();
    private static final Cart cart = new Cart();
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CartRepository cartRepository;
    @MockBean
    private ProductService productService;
    @MockBean
    private CartService cartService;
    @MockBean
    private UserService userService;
    @MockBean
    private AuthExceptionHandler authExceptionHandler;

    @BeforeAll
    public static void setUp() {
        user.setId(1L);
        user.setUsername("esoner.sezgin@gmail.com");
        user.setFirstName("soner");
        user.setLastName("sezgin");
        user.setPhoneNumber("+905332108093");
        user.setUserRole(UserRole.USER);

        cart.setUser(user);
    }

    @Test
    public void testGetProducts() throws Exception {
        List<Product> expected = createProducts();
        Mockito.when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(new Cart()));
        Mockito.when(productService.getAllProducts()).thenReturn(expected);
        Mockito.when(cartService.getCart(user)).thenReturn(cart);
        MvcResult mvcResult = mvc
                .perform(get("/api/products")
                        .with(user(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        ProductResponse productResponse = objectMapper.readValue(contentAsString, ProductResponse.class);
        assertThat(productResponse.getProducts()).containsExactlyInAnyOrderElementsOf(expected);
        assertThat(productResponse.getCart().getCartItems()).containsExactlyInAnyOrderElementsOf(cart.getCartItems());
    }

    private List<Product> createProducts() {
        Product p1 = new Product();
        p1.setName(product1Name);
        p1.setPrice(product1Price);
        p1.setType(product1Type);

        Product p2 = new Product();
        p2.setName(product2Name);
        p2.setPrice(product2Price);
        p2.setType(product2Type);

        List<Product> products = new ArrayList<>();
        products.add(p1);
        products.add(p2);

        return products;
    }
}
