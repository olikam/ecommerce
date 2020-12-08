package com.bestseller.ecommerce.model;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class AddItemRequest {

    @NotNull(message = "Drink id cannot be empty.")
    @PositiveOrZero(message = "Drink id must be 0 at least.")
    private Long drinkId;

    private List<Long> toppingIds = new ArrayList<>();

    @Min(value = 1, message = "Quantity must be 1 at least.")
    private Integer quantity;

    public Long getDrinkId() {
        return drinkId;
    }

    public void setDrinkId(Long drinkId) {
        this.drinkId = drinkId;
    }

    public List<Long> getToppingIds() {
        return toppingIds;
    }

    public void setToppingIds(List<Long> toppingIds) {
        this.toppingIds = toppingIds;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "AddItemRequest{" + "drinkId=" + drinkId + ", toppingIds=" + toppingIds + ", quantity=" + quantity + '}';
    }
}
