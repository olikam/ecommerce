package com.bestseller.ecommerce.service;

import com.bestseller.ecommerce.entity.Cart;
import com.bestseller.ecommerce.entity.CartItem;
import com.bestseller.ecommerce.entity.Product;
import com.bestseller.ecommerce.entity.User;
import com.bestseller.ecommerce.exception.ProductNotFoundException;
import com.bestseller.ecommerce.model.AddItemRequest;
import com.bestseller.ecommerce.model.DeleteItemRequest;
import com.bestseller.ecommerce.repository.CartItemRepository;
import com.bestseller.ecommerce.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private ProductService productService;

	@Autowired
	private DiscountService discountService;

	@Override
	public Cart getCart(User user) {
		Cart cart = cartRepository.findByUserId(user.getId()).orElseGet(() -> createNewCart(user));
		discountService.apply(cart);
		return cart;
	}

	@Override
	public Cart add(User user, AddItemRequest addItemRequest) {
		Cart cart = getCart(user);
		CartItem newCartItem = createCartItem(addItemRequest);
		newCartItem.setCart(cart);
		cart.getCartItems().stream()
				.filter(newCartItem::equals)
				.findAny()
				.ifPresentOrElse(cartItem -> cartItem.increaseQuantityBy(addItemRequest.getQuantity()),
						() -> cart.addCartItem(newCartItem));
		cartRepository.save(cart);
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
		return cartItemRepository.save(newCartItem);
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

	@Override
	public Cart delete(User user, DeleteItemRequest deleteItemRequest) {
		cartRepository.findByUserId(user.getId())
				.ifPresent(cart -> cart.getCartItems().stream()
						.filter(cartItem -> cartItem.getId() == deleteItemRequest.getCartItemId().longValue())
						.findAny()
						.ifPresent(cartItem -> {
							cart.removeCartItem(cartItem, deleteItemRequest.getQuantity());
							cartRepository.save(cart);
						}));
		return getCart(user);
	}

	@Override
	public void empty(User user) {
		cartRepository.findByUserId(user.getId()).ifPresent(cartRepository::delete);
	}
}
