package com.ecommerce.demo.service;

import com.ecommerce.demo.entity.Cart;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DiscountServiceImpl implements DiscountService {

    /**
     * Applies the discount rule to the cart.
     *
     * @param cart {@link Cart} object which will be applied discount rules on it.
     */
    @Override
    public void apply(Cart cart) {
        BigDecimal discountedAmount = Arrays.stream(DiscountRules.values())
                .map(rule -> rule.calculate(cart))
                .min(Comparator.naturalOrder())
                .orElse(cart.getTotalAmount())
                .setScale(2, RoundingMode.HALF_UP);
        cart.setDiscountedAmount(discountedAmount);
    }
}
