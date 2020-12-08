package com.bestseller.ecommerce.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bestseller.ecommerce.DemoApplication;
import com.bestseller.ecommerce.entity.Product;
import com.bestseller.ecommerce.entity.User;
import com.bestseller.ecommerce.model.ProductCreateRequest;
import com.bestseller.ecommerce.model.ProductDeleteRequest;
import com.bestseller.ecommerce.model.ProductType;
import com.bestseller.ecommerce.model.ProductUpdateRequest;
import com.bestseller.ecommerce.repository.ProductRepository;
import com.bestseller.ecommerce.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = DemoApplication.class)
@AutoConfigureMockMvc
public class AdminControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    private User admin;

    private User user;

    @BeforeEach
    public void setUp() {
        user = userRepository.findByUsername("esoner.sezgin@gmail.com").orElseThrow(AssertionError::new);
        admin = userRepository.findByUsername("paul.muaddib@arrakis.com").orElseThrow(AssertionError::new);
    }

    @Test
    public void testCreate() throws Exception {
        ProductCreateRequest request = createProductCreateRequest();

        mvc.perform(post("/api/admin/product")
                .with(user(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isCreated());
        Optional<Product> result = productRepository.findByNameIgnoreCase(request.getName());
        assertThat(result).isNotEmpty();
        assertThat(result.get().getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateNonAuthorized() throws Exception {
        ProductCreateRequest request = createProductCreateRequest();

        mvc.perform(post("/api/admin/product")
                .with(user(user))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUpdate() throws Exception {
        ProductUpdateRequest request = createProductUpdateRequest();

        mvc.perform(patch("/api/admin/product")
                .with(user(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isOk());
        Optional<Product> result = productRepository.findByNameIgnoreCase(request.getName());
        assertThat(result).isNotEmpty();
        assertThat(result.get().getId()).isEqualTo(request.getId());
        assertThat(result.get().getName()).isEqualTo(request.getName());
        assertThat(result.get().getPrice()).isEqualTo(BigDecimal.valueOf(7.0D).setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testUpdateWithExistingProductName() throws Exception {
        ProductUpdateRequest request = createProductUpdateRequest();
        request.setName("Milk");

        mvc.perform(patch("/api/admin/product")
                .with(user(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDelete() throws Exception {
        ProductDeleteRequest request = createProductDeleteRequest();

        mvc.perform(delete("/api/admin/product")
                .with(user(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isOk());
        Optional<Product> result = productRepository.findById(request.getProductId());
        assertThat(result).isEmpty();
    }

    private ProductDeleteRequest createProductDeleteRequest() {
        Product product = productRepository.findById(4L).orElseThrow(AssertionError::new);
        ProductDeleteRequest productDeleteRequest = new ProductDeleteRequest();
        productDeleteRequest.setProductId(product.getId());
        return productDeleteRequest;
    }

    private ProductCreateRequest createProductCreateRequest() {
        ProductCreateRequest request = new ProductCreateRequest();
        request.setName("Hot Chocolate");
        request.setPrice(7.0D);
        request.setType(ProductType.DRINK);
        return request;
    }

    private ProductUpdateRequest createProductUpdateRequest() {
        Product product = productRepository.findById(8L).orElseThrow(AssertionError::new);
        ProductUpdateRequest request = new ProductUpdateRequest();
        request.setId(product.getId());
        request.setName("Honey");
        request.setPrice(7.0D);
        request.setType(ProductType.TOPPING);
        return request;
    }

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
