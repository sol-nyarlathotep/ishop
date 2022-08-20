package org.sol.shop.dal;

import org.sol.shop.models.Order;

import java.sql.SQLException;
import java.util.List;

public class OrderDAO implements IOrderDAO{
    @Override
    public Order findById(Long id) throws SQLException {
        return null;
    }

    @Override
    public void save(Order order) throws SQLException {

    }

    @Override
    public void update(Order order) throws SQLException{

    }

    @Override
    public void delete(Order order) throws SQLException {

    }

    @Override
    public List<Order> findAll() throws SQLException {
        return null;
    }
}
