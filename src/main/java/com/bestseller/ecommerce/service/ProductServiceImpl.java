package com.bestseller.ecommerce.service;

import com.bestseller.ecommerce.entity.Product;
import com.bestseller.ecommerce.exception.DuplicateProductException;
import com.bestseller.ecommerce.exception.ProductNotFoundException;
import com.bestseller.ecommerce.model.ProductUpdateRequest;
import com.bestseller.ecommerce.repository.CartItemRepository;
import com.bestseller.ecommerce.repository.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	private static final Logger logger = LogManager.getLogger(ProductServiceImpl.class);

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Override
	public List<Product> getAllProducts() {
		return StreamSupport.stream(productRepository.findAll().spliterator(), false).collect(Collectors.toList());
	}

	@Override
	public Optional<Product> getProduct(Long id) {
		return productRepository.findById(id);
	}

	/**
	 * Creates a new product.
	 *
	 * @param product
	 *            {@link Product} details to be added.
	 */
	@Override
	public void create(Product product){
		productRepository.findByNameIgnoreCase(product.getName()).ifPresent(p -> {
			throw new DuplicateProductException(p.getName());
		});
		productRepository.save(product);
		logger.info("Product with the following id created successfully: " + product.getId());
	}

	/**
	 * Updates the specified product.
	 *
	 * @param productUpdateRequest
	 * 			  Details of the product to be updated.
	 */
	@Override
	public void update(ProductUpdateRequest productUpdateRequest) {
		Product product = productRepository.findById(productUpdateRequest.getId()).orElseThrow(() -> new ProductNotFoundException(productUpdateRequest.getId()));
		product.setName(productUpdateRequest.getName());
		product.setPrice(BigDecimal.valueOf(productUpdateRequest.getPrice()).setScale(2, RoundingMode.HALF_UP));
		product.setType(productUpdateRequest.getType());
		productRepository.save(product);
		logger.info("Product with the following id updated successfully: " + product.getId());
	}

	/**
	 * Deletes the product.
	 *
	 * @param productId
	 *            Product id which will be deleted.
	 */
	@Override
	public void delete(Long productId) {
		productRepository.findById(productId).ifPresent(product -> {
			product.getCartItem().forEach(cartItem -> {
				cartItem.getProducts().clear();
				cartItem.setCart(null);
				cartItemRepository.deleteById(cartItem.getId());
			});
			productRepository.delete(product);
		});
		logger.info("Product with the following id deleted successfully: " + productId);
	}

}
