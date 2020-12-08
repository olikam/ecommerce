package com.ecommerce.demo.service;

import com.ecommerce.demo.entity.Cart;
import com.ecommerce.demo.entity.CartItem;
import com.ecommerce.demo.entity.Order;
import com.ecommerce.demo.entity.OrderProduct;
import com.ecommerce.demo.entity.User;
import com.ecommerce.demo.model.OrderStatus;
import com.ecommerce.demo.model.ProductType;
import com.ecommerce.demo.repository.OrderProductRepository;
import com.ecommerce.demo.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LogManager.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private CartService cartService;

    @Override
    public List<Order> getOrders(User user) {
        return orderRepository.findByUsername(user.getUsername());
    }

    @Override
    public List<Order> getAllOrders() {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    /**
     * Places order using the cart for the specified user.
     *
     * @param user {@link User} object which will place order.
     * @return Returns {@link Order} details.
     */
    @Override
    public Order create(User user) {
        Order order = orderRepository.save(createOrderFromCart(user));
        cartService.empty(user);
        logger.info("Order created successfully - User: " + user.getId() + " - Order details: " + order);
        return order;
    }

    private Order createOrderFromCart(User user) {
        Order order = new Order();
        order.setDateCreated(LocalDateTime.now());
        order.setStatus(OrderStatus.PREPARING);
        Cart cart = cartService.getCart(user);
        List<OrderProduct> orderProducts = cart.getCartItems()
                .stream()
                .map(this::createOrderProduct)
                .collect(Collectors.toList());
        order.setOrderProducts(orderProducts);
        order.setTotalAmount(cart.getTotalAmount());
        order.setDiscountedAmount(cart.getDiscountedAmount());
        order.setUsername(user.getUsername());
        return order;
    }

    private OrderProduct createOrderProduct(CartItem cartItem) {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setPrice(cartItem.getAmount());
        orderProduct.setQuantity(cartItem.getQuantity());
        cartItem.getProducts().forEach(product -> {
            if (product.getType() == ProductType.DRINK) {
                orderProduct.setDrinkName(product.getName());
            } else {
                orderProduct.addTopping(product.getName());
            }
        });
        return orderProductRepository.save(orderProduct);
    }
}
