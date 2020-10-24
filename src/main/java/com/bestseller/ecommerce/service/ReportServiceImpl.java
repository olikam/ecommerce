package com.bestseller.ecommerce.service;

import com.bestseller.ecommerce.entity.Order;
import com.bestseller.ecommerce.entity.OrderProduct;
import com.bestseller.ecommerce.model.Amount;
import com.bestseller.ecommerce.model.Report;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {

	@Autowired
	private OrderService orderService;

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
				Arrays.stream(orderProduct.getToppingsNames().split(",")).filter(StringUtils::isNotBlank).forEach(topping -> mostUsedToppings.put(topping, mostUsedToppings.getOrDefault(topping, 1) + orderProduct.getQuantity()));
			}
		}
		return new Report(sortByDesc(amountPerCustomer), sortByDesc(mostUsedToppings));
	}

	private <K, V extends Comparable<V>> Map<K, V> sortByDesc(Map<K, V> map) {
		return map.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
	}
}
