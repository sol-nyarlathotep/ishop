package org.sol.shop.services;

import org.sol.shop.dal.OrderDAO;
import org.sol.shop.dal.UserDAO;
import org.sol.shop.models.Order;
import org.sol.shop.models.Product;
import org.sol.shop.models.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class OrderService implements IOrderService {

    OrderDAO orderDAO = new OrderDAO();
    UserDAO userDAO = new UserDAO();

    public OrderService() throws SQLException {
    }

    @Override
    public List<Order> loadUserOrders(User user) {
        List<Order> orderList;
        try {
            orderList = orderDAO.findOrdersByUser(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orderList;
    }

    @Override
    public void cancelOrder(User runAsUser, Order order) {
        if (Objects.equals(order.getUserId(), runAsUser.getId()) || runAsUser.isAdmin()){
            order.setStatus(Order.OrderStatus.CANCELLED);
            try {
                orderDAO.update(order);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("You need to be an admin or order owner to cancel it.");
    }

    @Override
    public void orderCheckout(User runAsUser) {
        // TODO: From userCart checkout.
        HashMap<Product, Long> userCart = runAsUser.getUserCart();
        Order order = new Order(runAsUser.getId(), Order.OrderStatus.CHECKOUTED, new HashMap<>(userCart));
        try {
            orderDAO.save(order);
            // TODO: CLEAN THE CART
            runAsUser.cleanCart();
            userDAO.update(runAsUser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void confirmOrder(User runAsUser, Order order) {
        if(runAsUser.isAdmin()){
            if(order.getStatus() != Order.OrderStatus.CHECKOUTED){
                throw new RuntimeException("You can only confirm an order in a checkout state.");
            }
            order.setStatus(Order.OrderStatus.CONFIRMED);
            try {
                orderDAO.update(order);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("You need to be an admin to confirm the order.");
    }

    @Override
    public void unConfirmOrder(User runAsUser, Order order) {
        if(runAsUser.isAdmin()){
            if(order.getStatus() != Order.OrderStatus.CHECKOUTED){
                throw new RuntimeException("You can only un-confirm an order in checkout state.");
            }
            order.setStatus(Order.OrderStatus.UNCONFIRMED);
            try {
                orderDAO.update(order);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("You need to be an admin to un-confirm the order.");
    }
}
