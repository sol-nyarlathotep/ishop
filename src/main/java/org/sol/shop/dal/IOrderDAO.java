package org.sol.shop.dal;

import org.sol.shop.models.Order;

import java.sql.SQLException;
import java.util.List;

public interface IOrderDAO {

    Order findById(Long id) throws SQLException;
    void save(Order order) throws SQLException;
    void update(Order order) throws SQLException;
    void delete(Order order) throws SQLException;
    List<Order> findAll() throws SQLException;

}
