package com.ecommerce.demo.model;

public class DeleteItemRequest {

    private Long id;

    private Integer quantity;

    public DeleteItemRequest(Long id, Integer quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "DeleteItemRequest{" + "cartItemId=" + id + ", quantity=" + quantity + '}';
    }
}
