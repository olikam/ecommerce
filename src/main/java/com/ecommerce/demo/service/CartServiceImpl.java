package com.ecommerce.demo.service;

import com.ecommerce.demo.entity.Cart;
import com.ecommerce.demo.entity.CartItem;
import com.ecommerce.demo.entity.Product;
import com.ecommerce.demo.entity.User;
import com.ecommerce.demo.exception.ProductNotFoundException;
import com.ecommerce.demo.model.AddItemRequest;
import com.ecommerce.demo.model.DeleteItemRequest;
import com.ecommerce.demo.repository.CartItemRepository;
import com.ecommerce.demo.repository.CartRepository;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private static final Logger logger = LogManager.getLogger(CartServiceImpl.class);

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private DiscountService discountService;

    /**
     * @param user {@link User} object having the cart.
     * @return Returns cart of the specified customer.
     */
    @Override
    public Cart getCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId()).orElseGet(() -> createNewCart(user));
        discountService.apply(cart);
        return cart;
    }

    /**
     * Adds the specified item to the cart.
     *
     * @param user           {@link User} object to add item.
     * @param addItemRequest {@link AddItemRequest} object to be added to the cart.
     * @return Returns up-to-date cart of the user.
     */
    @Override
    public Cart add(User user, AddItemRequest addItemRequest) {
        Cart cart = getCart(user);
        CartItem newCartItem = createCartItem(addItemRequest);
        newCartItem.setCart(cart);
        if (!cart.getCartItems().contains(newCartItem)) {
            cartItemRepository.save(newCartItem);
        }
        cart.addCartItem(newCartItem);
        cartRepository.save(cart);
        logger.info("CartItem added successfully - User: " + user.getId() + " - Added item: " + addItemRequest);
        return getCart(user);
    }

    private Cart createNewCart(User user) {
        Cart newCart = new Cart();
        newCart.setUser(user);
        return cartRepository.save(newCart);
    }

    private CartItem createCartItem(AddItemRequest addItemRequest) {
        CartItem newCartItem = new CartItem();
        newCartItem.setProducts(getProductsByIds(addItemRequest), addItemRequest.getQuantity());
        return newCartItem;
    }

    private List<Product> getProductsByIds(AddItemRequest addItemRequest) {
        List<Product> products = new ArrayList<>();
        Product drink = productService.getProduct(addItemRequest.getDrinkId())
                .orElseThrow(() -> new ProductNotFoundException(addItemRequest.getDrinkId()));
        products.add(drink);

        addItemRequest.getToppingIds().stream()
                .map(id -> productService.getProduct(id).orElseThrow(() -> new ProductNotFoundException(id)))
                .sorted()
                .forEach(products::add);

        return products;
    }

    /**
     * Deletes the specified item from the cart.
     *
     * @param user              {@link User} object to delete item.
     * @param deleteItemRequest {@link DeleteItemRequest} object to be deleted from the cart.
     * @return Returns up-to-date cart of the user.
     */
    @Override
    public Cart delete(User user, DeleteItemRequest deleteItemRequest) {
        cartRepository.findByUserId(user.getId())
                .ifPresent(cart -> cart.getCartItems().stream()
                        .filter(cartItem -> cartItem.getId() == deleteItemRequest.getId().longValue())
                        .findAny()
                        .ifPresent(cartItem -> {
                            cart.removeCartItem(cartItem, deleteItemRequest.getQuantity());
                            cartRepository.save(cart);
                        }));
        logger.info("CartItem deleted successfully - User: " + user.getId() + " - Deleted item: " + deleteItemRequest);
        return getCart(user);
    }

    /**
     * Empties the cart of the user.
     *
     * @param user {@link User} object to empty the cart.
     */
    @Override
    public void empty(User user) {
        cartRepository.findByUserId(user.getId()).ifPresent(cart -> {
            cart.getCartItems().forEach(cartItem -> cartItem.setCart(null));
            cartItemRepository.deleteAll(cart.getCartItems());
            cartRepository.delete(cart);
            logger.info("Cart is emptied successfully - User: " + user.getId());
        });
    }
}
