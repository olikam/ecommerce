package com.ecommerce.demo.model;

public enum Currency {

    EUR("eur"), USD("usd"), TRY("try");

    private String text;

    Currency(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
