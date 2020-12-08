package com.ecommerce.demo.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ecommerce.demo.DemoApplication;
import com.ecommerce.demo.entity.User;
import com.ecommerce.demo.model.LoginRequest;
import com.ecommerce.demo.model.UserRole;
import com.ecommerce.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.assertj.core.api.Assertions;
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
public class LoginControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User existingUser;

    private User newUser = new User();

    @BeforeEach
    public void setUp() {
        existingUser = userRepository.findByUsername("esoner.sezgin@gmail.com").orElseThrow(AssertionError::new);
        newUser.setUsername("eru.illuvatar@valinor.com");
        newUser.setFirstName("eru");
        newUser.setLastName("illuvatar");
        newUser.setPhoneNumber("999999");
        newUser.setUserRole(UserRole.USER);
    }

    @Test
    public void testRegister() throws Exception {
        MvcResult mvcResult = mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newUser)))
                .andExpect(status().isOk())
                .andReturn();
        Optional<User> result = userRepository.findByUsername(newUser.getUsername());
        assertThat(result).isNotEmpty();
        assertThat(result.get().getUsername()).isEqualTo(newUser.getUsername());
        assertThat(result.get().getPhoneNumber()).isEqualTo(newUser.getPhoneNumber());
        assertThat(result.get().getFirstName()).isEqualTo(newUser.getFirstName());
        assertThat(result.get().getLastName()).isEqualTo(newUser.getLastName());
        Assertions.assertThat(result.get().getUserRole()).isEqualTo(newUser.getUserRole());
    }

    @Test
    public void testRegisterExisting() throws Exception {
        MvcResult mvcResult = mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(existingUser)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).contains("already registered");
    }

    @Test
    public void testLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(existingUser.getUsername());
        loginRequest.setPassword(existingUser.getPassword().replace("{ldap}", ""));
        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(loginRequest)))
                .andExpect(status().isOk());
    }

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
