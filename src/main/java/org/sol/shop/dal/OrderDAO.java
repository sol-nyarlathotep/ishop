package org.sol.shop.dal;

import org.sol.shop.models.Order;

import java.util.List;

public interface OrderDAO {

    Order findById(Long id);
    void save(Order order);
    void update(Order order);
    void delete(Order order);
    List<Order> findAll();

}
