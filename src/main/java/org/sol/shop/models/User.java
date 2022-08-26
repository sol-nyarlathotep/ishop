package org.sol.shop.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User {
    private Long id;
    private String login, password;
    private boolean isAdmin, blocked;
    private HashMap<Product, Long> userCart = new HashMap<>(); // Product: Count

    // TODO: Maybe make Set?
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
        if (order.getOrderProducts().isEmpty()){
            throw new RuntimeException("You cannot add an order that does not have products");
        }
        userOrders.add(order);
    }


    public void cleanCart() throws SQLException {
        this.setUserCart(new HashMap<>());
    }


    public void addToCart(Product product, Long count){
        if(userCart.containsKey(product)){
            userCart.replace(product, userCart.get(product)+count);
            return;
        }
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
        if (id == null){
            throw new RuntimeException("The user has not been saved to the database. ID is unavailable.");
        }
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HashMap<Product, Long> getUserCart() {
        return userCart;
    }

    public void setUserCart(HashMap<Product, Long> userCart) {
        this.userCart = userCart;
    }

    public List<Order> getUserOrders() {
        return userOrders;
    }

    public void setUserOrders(List<Order> userOrders) {
        this.userOrders = userOrders;
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

        return getId().equals(user.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
