package com.ecommerce.demo.service;

import com.ecommerce.demo.entity.Product;
import com.ecommerce.demo.exception.DuplicateProductException;
import com.ecommerce.demo.exception.ProductNotFoundException;
import com.ecommerce.demo.model.ProductUpdateRequest;
import com.ecommerce.demo.repository.CartItemRepository;
import com.ecommerce.demo.repository.ProductRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @param product {@link Product} details to be added.
     */
    @Override
    public void create(Product product) {
        validateUniqueProductName(product.getName());
        productRepository.save(product);
        logger.info("Product with the following id created successfully: " + product.getId());
    }

    /**
     * Updates the specified product.
     *
     * @param productUpdateRequest Details of the product to be updated.
     */
    @Override
    public void update(ProductUpdateRequest productUpdateRequest) {
        validateUniqueProductName(productUpdateRequest.getName());
        Product product = productRepository.findById(productUpdateRequest.getId()).orElseThrow(() -> new ProductNotFoundException(productUpdateRequest.getId()));
        product.setName(productUpdateRequest.getName());
        product.setPrice(BigDecimal.valueOf(productUpdateRequest.getPrice()).setScale(2, RoundingMode.HALF_UP));
        product.setType(productUpdateRequest.getType());
        productRepository.save(product);
        logger.info("Product with the following id updated successfully: " + product.getId());
    }

    private void validateUniqueProductName(String name) {
        productRepository.findByNameIgnoreCase(name).ifPresent(p -> {
            throw new DuplicateProductException(p.getName());
        });
    }

    /**
     * Deletes the product.
     *
     * @param productId Product id which will be deleted.
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
