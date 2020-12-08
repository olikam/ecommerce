package com.ecommerce.demo.service;

import com.ecommerce.demo.entity.Order;
import com.ecommerce.demo.entity.OrderProduct;
import com.ecommerce.demo.model.Amount;
import com.ecommerce.demo.model.Report;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {

    private static final Logger logger = LogManager.getLogger(ReportServiceImpl.class);

    @Autowired
    private OrderService orderService;

    /**
     * Generates report.
     *
     * @return Returns {@link Report}
     */
    @Override
    public Report generateReport() {
        Map<String, Amount> amountPerCustomer = new HashMap<>();
        Map<String, Integer> mostUsedToppings = new HashMap<>();
        for (Order order : orderService.getAllOrders()) {
            String username = order.getUsername();
            Amount amount = amountPerCustomer.computeIfAbsent(username, k -> new Amount());
            amount.addToTotal(order.getTotalAmount());
            amount.addToDiscounted(order.getDiscountedAmount());
            for (OrderProduct orderProduct : order.getOrderProducts()) {
                Arrays.stream(orderProduct.getToppingsNames().split(",")).filter(StringUtils::isNotBlank)
                        .forEach(topping -> mostUsedToppings.put(topping, mostUsedToppings.getOrDefault(topping, 0) + orderProduct.getQuantity()));
            }
        }
        Report report = new Report(sortByDesc(amountPerCustomer), sortByDesc(mostUsedToppings));
        logger.info("Report generated successfully: " + report);
        return report;
    }

    private <K, V extends Comparable<V>> Map<K, V> sortByDesc(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
}
