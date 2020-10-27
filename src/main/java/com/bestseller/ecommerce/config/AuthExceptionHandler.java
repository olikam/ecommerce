package com.bestseller.ecommerce.config;

import com.bestseller.ecommerce.service.ProductServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthExceptionHandler implements AuthenticationEntryPoint {

	private static final Logger logger = LogManager.getLogger(AuthenticationEntryPoint.class);

	@Override
	public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e)
			throws IOException, ServletException {
		logger.error("Unauthorized error: {}", e.getMessage());
		httpServletResponse.setContentType("application/json");
		httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		httpServletResponse.getOutputStream().println("{ \"Authentication error\": \"" + e.getMessage() + "\" }");
	}
}
