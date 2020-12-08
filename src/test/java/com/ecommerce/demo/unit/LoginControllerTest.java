package com.ecommerce.demo.unit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ecommerce.demo.config.AuthExceptionHandler;
import com.ecommerce.demo.controller.LoginController;
import com.ecommerce.demo.entity.User;
import com.ecommerce.demo.exception.UserAlreadyExistsException;
import com.ecommerce.demo.model.UserRole;
import com.ecommerce.demo.service.ProductService;
import com.ecommerce.demo.service.ReportService;
import com.ecommerce.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LoginController.class)
@ActiveProfiles("test")
public class LoginControllerTest {

    private static final User admin = new User();
    private static final User user = new User();
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserService userService;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private AuthExceptionHandler authExceptionHandler;
    @MockBean
    private ProductService productService;
    @MockBean
    private ReportService reportService;

    @BeforeAll
    public static void setUp() {
        admin.setUsername("paul.muaddib@arrakis.com");
        admin.setPassword("1");
        admin.setFirstName("paul");
        admin.setLastName("muaddib");
        admin.setPhoneNumber("00000000000");
        admin.setUserRole(UserRole.ADMIN);

        user.setUsername("esoner.sezgin@gmail.com");
        user.setPassword("1");
        user.setFirstName("soner");
        user.setLastName("sezgin");
        user.setPhoneNumber("+905332108093");
        user.setUserRole(UserRole.USER);

    }

    @Test
    public void testRegister() throws Exception {
        mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user)))
                .andExpect(status().isOk());
    }

    @Test
    public void testRegisterWithExistingUser() throws Exception {
        Mockito.doThrow(new UserAlreadyExistsException(user.getUsername())).when(userService).register(ArgumentMatchers.any(User.class));
        MvcResult mvcResult = mvc.perform(
                post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("This user is already registered: " + user.getUsername());
    }

    @Test
    public void testLogin() throws Exception {
        Authentication authentication = new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority(user.getUserRole().name()));
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return user;
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(boolean b) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return user.getUsername();
            }
        };
        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()))).thenReturn(authentication);
        MvcResult mvcResult = mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user)))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).isNotBlank();
    }

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
