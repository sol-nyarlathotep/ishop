package org.sol.shop.models;

import java.util.HashMap;

public class Order implements Entity{ // TODO: Remove "Entity" if it redundant.

    private Long id, userId;
    private OrderStatus status;
    private HashMap<Product, Long> orderProducts; // Product: Count

    public Order(Long id, Long userId, OrderStatus status) {
        this.id = id;
        this.userId = userId;
        this.status = status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (!id.equals(order.id)) return false;
        return userId.equals(order.userId);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + userId.hashCode();
        return result;
    }

    public enum OrderStatus{
        PROCESSED,
        TRANSPORTING,
        CANCELLED,
        DELIVERED

    }

}
