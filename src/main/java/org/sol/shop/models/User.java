package org.sol.shop.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User {
    private Long id;
    private String login, password;
    private boolean isAdmin, blocked;
    private HashMap<Product, Long> userCart; // Product: Count
    private List<Order> userOrders = new ArrayList<>();

    public User(Long id, String login, String password, boolean isAdmin, boolean blocked) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.isAdmin = isAdmin;
        this.blocked = blocked;
    }

    public User(String login, String password, boolean isAdmin, boolean blocked) {
        this.login = login;
        this.password = password;
        this.isAdmin = isAdmin;
        this.blocked = blocked;
    }

    public void addToOrders(Order order){
        userOrders.add(order);
    }

    public void addToCart(Product product, Long count){
        userCart.put(product, count);
    }

    public void removeFromCart(Product product, Long count){
        if(!userCart.containsKey(product)){
            throw new RuntimeException("This product is not in the cart");
        }
        if (count < 1){
            throw new IllegalArgumentException("Count must be gte 1");
        }
        Long productCount = userCart.get(product);
        if (count > productCount){
            throw new IllegalArgumentException("The count must not exceed the count of product in the cart");
        }
        if (count.equals(productCount)){
            userCart.remove(product);
            return;
        }
        userCart.replace(product, productCount-count);
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
