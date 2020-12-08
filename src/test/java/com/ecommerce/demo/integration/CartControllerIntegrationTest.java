package com.ecommerce.demo.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ecommerce.demo.DemoApplication;
import com.ecommerce.demo.entity.Cart;
import com.ecommerce.demo.entity.CartItem;
import com.ecommerce.demo.entity.Product;
import com.ecommerce.demo.entity.User;
import com.ecommerce.demo.model.AddItemRequest;
import com.ecommerce.demo.model.DeleteItemRequest;
import com.ecommerce.demo.model.ProductType;
import com.ecommerce.demo.repository.CartRepository;
import com.ecommerce.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = DemoApplication.class)
@AutoConfigureMockMvc
public class CartControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user1;

    private User user2;

    @BeforeEach
    public void setUp() {
        user1 = userRepository.findByUsername("esoner.sezgin@gmail.com").orElseThrow(AssertionError::new);
        user2 = userRepository.findByUsername("paul.muaddib@arrakis.com").orElseThrow(AssertionError::new);
    }

    @Test
    public void testGetCart() throws Exception {
        mvc.perform(get("/api/cart")
                .with(user(user1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddItem() throws Exception {
        int quantity = 2;
        Long drinkId = 1L;
        List<Long> toppingIds = List.of(5L, 6L);
        addItem(user1, quantity, drinkId, toppingIds);
        Optional<Cart> result = cartRepository.findByUserId(user1.getId());
        assertThat(result).isNotEmpty();
        assertThat(result.get().getCartItems().size()).isEqualTo(1);

        CartItem cartItem = result.get().getCartItems().iterator().next();
        assertThat(cartItem.getQuantity()).isEqualTo(quantity);
        assertThat(cartItem.getProducts().size()).isEqualTo(3);

        Optional<Product> optionalDrink = cartItem.getProducts().stream().filter(p -> p.getType() == ProductType.DRINK).findAny();
        assertThat(optionalDrink).isNotEmpty();
        assertThat(optionalDrink.get().getId()).isEqualTo(1);

        cartItem.getProducts().stream().filter(p -> p.getType() == ProductType.TOPPING).forEach(topping -> {
            if (!toppingIds.contains(topping.getId())) {
                throw new AssertionError();
            }
        });
    }

    @Test
    public void testDeleteItem() throws Exception {
        int quantity = 2;
        Long drinkId = 3L;
        List<Long> toppingIds = List.of(6L, 7L);
        Cart cart = addItem(user2, quantity, drinkId, toppingIds);
        int deletedQuantity = 1;
        DeleteItemRequest deleteItemRequest = new DeleteItemRequest(cart.getCartItems().iterator().next().getId(), deletedQuantity);

        mvc.perform(delete("/api/cart/item/{id}?quantity={quantity}", deleteItemRequest.getId(), deleteItemRequest.getQuantity())
                .with(user(user2)))
                .andExpect(status().isOk());

        Optional<Cart> result = cartRepository.findByUserId(user2.getId());
        assertThat(result).isNotEmpty();
        assertThat(cart.getCartItems().iterator().next().getQuantity() - deletedQuantity).isEqualTo(result.get().getCartItems().size());
    }

    @Test
    public void testEmpty() throws Exception {
        int quantity = 2;
        Long drinkId = 3L;
        List<Long> toppingIds = List.of(6L, 7L);
        addItem(user2, quantity, drinkId, toppingIds);
        mvc.perform(delete("/api/cart/items")
                .with(user(user2))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<Cart> result = cartRepository.findByUserId(user2.getId());
        assertThat(result).isEmpty();
    }

    private Cart addItem(User user, int quantity, Long drinkId, List<Long> toppingIds) throws Exception {
        AddItemRequest addItemRequest = new AddItemRequest();
        addItemRequest.setDrinkId(drinkId);
        addItemRequest.setToppingIds(toppingIds);
        addItemRequest.setQuantity(quantity);
        MvcResult mvcResult = mvc.perform(post("/api/cart/item")
                .with(user(user))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(addItemRequest)))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(contentAsString, Cart.class);
    }

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
