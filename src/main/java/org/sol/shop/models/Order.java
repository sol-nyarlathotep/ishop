package org.sol.shop.models;

import java.util.HashMap;

public class Order {

    private Long id, user_id;
    private OrderStatus status;
    private HashMap<Product, Long> orderProducts; // Product: Count

    public Order(Long id, Long user_id, OrderStatus status) {
        this.id = id;
        this.user_id = user_id;
        this.status = status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getUser_id() {
        return user_id;
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
        return user_id.equals(order.user_id);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + user_id.hashCode();
        return result;
    }

    public enum OrderStatus{
        PROCESSED,
        TRANSPORTING,
        CANCELLED,
        DELIVERED
    }

}
