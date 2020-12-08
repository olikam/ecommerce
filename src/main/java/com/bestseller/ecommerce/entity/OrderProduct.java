package com.bestseller.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.apache.commons.lang3.StringUtils;

@Entity
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String drinkName;

    private String toppingsNames = "";

    private BigDecimal price;

    private Integer quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public String getToppingsNames() {
        return toppingsNames;
    }

    public void setToppingsNames(String toppingsNames) {
        this.toppingsNames = toppingsNames;
    }

    public void addTopping(String toppingName) {
        if (StringUtils.isNotBlank(toppingsNames)) {
            toppingsNames += ",";
        }
        toppingsNames += toppingName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderProduct{" + "drinkName='" + drinkName + '\'' + ", toppingsNames='" + toppingsNames + '\'' + ", price=" + price + ", quantity=" + quantity + '}';
    }
}
