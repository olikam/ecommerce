package com.bestseller.ecommerce.model;

import java.util.Map;

public class Report {

    private Map<String, Amount> amountPerCustomer;

    private Map<String, Integer> mostUsedToppings;

    public Report(Map<String, Amount> amountPerCustomer, Map<String, Integer> mostUsedToppings) {
        this.amountPerCustomer = amountPerCustomer;
        this.mostUsedToppings = mostUsedToppings;
    }

    public Map<String, Amount> getAmountPerCustomer() {
        return amountPerCustomer;
    }

    public void setAmountPerCustomer(Map<String, Amount> amountPerCustomer) {
        this.amountPerCustomer = amountPerCustomer;
    }

    public Map<String, Integer> getMostUsedToppings() {
        return mostUsedToppings;
    }

    public void setMostUsedToppings(Map<String, Integer> mostUsedToppings) {
        this.mostUsedToppings = mostUsedToppings;
    }

    @Override
    public String toString() {
        return "Report{" + "amountPerCustomer=" + amountPerCustomer + ", mostUsedToppings=" + mostUsedToppings + '}';
    }
}
