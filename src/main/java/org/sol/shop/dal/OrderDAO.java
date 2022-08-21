package org.sol.shop.dal;

import org.sol.shop.models.Order;
import org.sol.shop.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderDAO implements IOrderDAO{

    Connection con = DBUtils.getConnection();
    private PreparedStatement insertPreparedSt = con.prepareStatement("INSERT INTO orders (users_id, status) VALUES (?, ?)");
    private PreparedStatement selectPreparedSt = con.prepareStatement("SELECT * FROM orders WHERE id=?");
    private PreparedStatement updatePreparedSt = con.prepareStatement("UPDATE orders SET status=? WHERE id=?");
    private PreparedStatement deletePreparedSt = con.prepareStatement("DELETE FROM orders WHERE id=?");

    public OrderDAO() throws SQLException {
    }


    @Override
    public Order findById(Long id) throws SQLException {
        Order order = null;
        selectPreparedSt.setLong(1, id);
        boolean hasResult = selectPreparedSt.execute();
        if(hasResult){
            ResultSet rs = selectPreparedSt.getResultSet();
            rs.next();
            Long userId = rs.getLong("users_id");
            Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(rs.getString("status"));
            order = new Order(id, userId, orderStatus);
        }
        return order;
    }

    @Override
    public void save(Order order) throws SQLException {
        var orderUserId = order.getUserId();
        var orderStatus = order.getStatus().toString();
        insertPreparedSt.setLong(1, orderUserId);
        insertPreparedSt.setString(2, orderStatus);
        insertPreparedSt.executeUpdate();
    }

    @Override
    public void update(Order order) throws SQLException{
        Order or = findById(order.getId());
        if(or == null){
            throw new RuntimeException("Errors occurred while find this object");
        }
        var orderStatus = order.getStatus().toString();
        updatePreparedSt.setString(1, orderStatus);
        updatePreparedSt.setLong(2, order.getId());
        updatePreparedSt.executeUpdate();
    }

    @Override
    public void delete(Order order) throws SQLException {
        Order or = findById(order.getId());
        if(or == null){
            throw new RuntimeException("Errors occurred while find this object");
        }
        deletePreparedSt.setLong(1, order.getId());
        deletePreparedSt.executeUpdate();
    }

    @Override
    public List<Order> findAll() throws SQLException {
        return null;
    }
}
