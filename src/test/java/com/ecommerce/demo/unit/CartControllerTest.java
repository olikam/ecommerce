package com.ecommerce.demo.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ecommerce.demo.config.AuthExceptionHandler;
import com.ecommerce.demo.controller.CartController;
import com.ecommerce.demo.entity.Cart;
import com.ecommerce.demo.entity.User;
import com.ecommerce.demo.repository.CartItemRepository;
import com.ecommerce.demo.repository.CartRepository;
import com.ecommerce.demo.repository.UserRepository;
import com.ecommerce.demo.service.CartService;
import com.ecommerce.demo.service.DiscountService;
import com.ecommerce.demo.service.ProductService;
import com.ecommerce.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@WebMvcTest(CartController.class)
@WithMockUser
@ActiveProfiles("test")
public class CartControllerTest {

    private static final User user = new User();
    private static final Cart cart = new Cart();
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ProductService productService;
    @MockBean
    private CartService cartService;
    @MockBean
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AuthExceptionHandler authExceptionHandler;
    @MockBean
    private CartRepository cartRepository;
    @MockBean
    private CartItemRepository cartItemRepository;
    @MockBean
    private DiscountService discountService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetCart() throws Exception {
        Mockito.when(cartService.getCart(user)).thenReturn(cart);
        MvcResult mvcResult = mvc
                .perform(get("/api/cart")
                        .with(user(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        Cart actual = objectMapper.readValue(contentAsString, Cart.class);
        assertThat(actual.getCartItems()).containsExactlyInAnyOrderElementsOf(cart.getCartItems());

    }

}
