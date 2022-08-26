package org.sol.shop.services;

import org.sol.shop.models.Order;
import org.sol.shop.models.User;

import java.util.List;

public interface IOrderService {

    List<Order> loadUserOrders(User user);
    void cancelOrder(User runAsUser, Order order); // TODO: Admins and owners
    void orderCheckout(User runAsUser); // TODO: Only owners

    // Admins
    void confirmOrder(User runAsUser, Order order);
    void unConfirmOrder(User runAsUser, Order order);

}
