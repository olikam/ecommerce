package com.bestseller.ecommerce;

import com.bestseller.ecommerce.config.AuthExceptionHandler;
import com.bestseller.ecommerce.controller.AdminController;
import com.bestseller.ecommerce.entity.User;
import com.bestseller.ecommerce.model.DeleteProductRequest;
import com.bestseller.ecommerce.model.ProductCreateUpdateRequest;
import com.bestseller.ecommerce.model.ProductType;
import com.bestseller.ecommerce.model.UserRole;
import com.bestseller.ecommerce.service.ProductService;
import com.bestseller.ecommerce.service.ReportService;
import com.bestseller.ecommerce.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AdminController.class)
@WithMockUser
@ActiveProfiles("test")
public class AdminControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ProductService productService;

	@MockBean
	private ReportService reportService;

	@MockBean
	private UserService userService;

	@MockBean
	private AuthExceptionHandler authExceptionHandler;

	@Autowired
	private ObjectMapper objectMapper;

	private static final User admin = new User();

	private static final User user = new User();

	@BeforeAll
	public static void setUp() {
		admin.setId(1L);
		admin.setUsername("paul.muaddib@arrakis.com");
		admin.setFirstName("paul");
		admin.setLastName("muaddib");
		admin.setPhoneNumber("00000000000");
		admin.setUserRole(UserRole.ADMIN);

		user.setId(2L);
		user.setUsername("esoner.sezgin@gmail.com");
		user.setFirstName("soner");
		user.setLastName("sezgin");
		user.setPhoneNumber("+905332108093");
		user.setUserRole(UserRole.USER);
	}

	@Test
	public void testCreate() throws Exception {
		ProductCreateUpdateRequest request = createProductRequest();

		mvc.perform(post("/api/admin")
				.with(user(admin))
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(request)))
				.andExpect(status().isCreated());
	}

	@Test
	public void testCreateUnauthorized() throws Exception {
		ProductCreateUpdateRequest request = createProductRequest();

		mvc.perform(post("/api/admin")
				.with(user(user))
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(request)))
				.andExpect(status().isForbidden());
	}

	@Test
	public void testUpdate() throws Exception {
		ProductCreateUpdateRequest request = createProductRequest();

		mvc.perform(put("/api/admin")
				.with(user(admin))
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(request)))
				.andExpect(status().isOk());
	}

	@Test
	public void testUpdateUnauthorized() throws Exception {
		ProductCreateUpdateRequest request = createProductRequest();

		mvc.perform(put("/api/admin")
				.with(user(user))
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(request)))
				.andExpect(status().isForbidden());
	}

	@Test
	public void testDeleteUnauthorized() throws Exception {
		mvc.perform(delete("/api/admin")
				.with(user(user))
				.contentType(MediaType.APPLICATION_JSON)
				.param("productId", "5"))
				.andExpect(status().isForbidden());
	}

	@Test
	public void testDelete() throws Exception {
		DeleteProductRequest request = new DeleteProductRequest();
		request.setProductId(5L);

		mvc.perform(delete("/api/admin")
				.with(user(admin))
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(request)))
				.andExpect(status().isOk());
	}

	private ProductCreateUpdateRequest createProductRequest() {
		ProductCreateUpdateRequest request = new ProductCreateUpdateRequest();
		request.setName("Hot Chocolate");
		request.setPrice(7.0D);
		request.setType(ProductType.DRINK);
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
