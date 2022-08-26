package org.sol.shop.models;

import java.util.HashMap;

public class Order{

    private Long id, userId;
    private OrderStatus status;
    private HashMap<Product, Long> orderProducts = new HashMap<>(); // Product: Count

    public Order(Long id, Long userId, OrderStatus status) {
        this.id = id;
        this.userId = userId;
        this.status = status;
    }

    public Order(Long userId, OrderStatus status) {
        this.userId = userId;
        this.status = status;
    }

    public Order(Long userId, OrderStatus status, HashMap<Product, Long> orderProducts) {
        this.userId = userId;
        this.status = status;
        this.orderProducts = orderProducts;
    }

    public HashMap<Product, Long> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(HashMap<Product, Long> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public Long getId() {
        if (id == null){
            throw new RuntimeException("The order has not been saved to the database. ID is unavailable.");
        }
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void addToProducts(Product product, Long count){
        if(orderProducts.containsKey(product)){
            orderProducts.replace(product, orderProducts.get(product)+count);
            return;
        }
        orderProducts.put(product, count);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        return getId().equals(order.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    public enum OrderStatus{
        CHECKOUTED,
        UNCONFIRMED,
        CONFIRMED,
        PROCESSED,
        TRANSPORTING,
        CANCELLED,
        DELIVERED
    }

}
