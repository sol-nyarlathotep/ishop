package org.sol.shop.dal;

import org.sol.shop.models.Order;
import org.sol.shop.models.Product;
import org.sol.shop.models.User;
import org.sol.shop.utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderDAO implements IOrderDAO{

    Connection con = DBUtils.getConnection();
    private PreparedStatement insertPreparedSt = con.prepareStatement("INSERT INTO orders (users_id, status) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
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
            if(rs.next()){
                Long userId = rs.getLong("users_id");
                Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(rs.getString("status"));
                order = new Order(id, userId, orderStatus);
                var orderProducts = getOrderProducts(order);
                order.setOrderProducts(orderProducts);
            }
        }
        return order;
    }

    @Override
    public void save(Order order) throws SQLException {
        {
            var orderUserId = order.getUserId();
            var orderStatus = order.getStatus().toString();
            insertPreparedSt.setLong(1, orderUserId);
            insertPreparedSt.setString(2, orderStatus);
            insertPreparedSt.execute();
        }
        var rs = insertPreparedSt.getGeneratedKeys();
        rs.next();
        order.setId(rs.getLong(1));
        saveOrderProducts(order);
    }

    private void saveOrderProducts(Order order) throws SQLException{
        // TODO: OrderProducts Save | 1 field - orderProducts +completed
        PreparedStatement insertOrderProductsPreparedSt = con.prepareStatement("INSERT INTO order_products (products_id, count, orders_id) VALUES (?, ?, ?)");
        HashMap<Product, Long> orderProducts = order.getOrderProducts();
        orderProducts.forEach((product, count)->{
            try {
                if(!orderProductIsPresentInDB(product)){
                    insertOrderProductsPreparedSt.setLong(1, product.getId());
                    insertOrderProductsPreparedSt.setLong(2, count);
                    insertOrderProductsPreparedSt.setLong(3, order.getId());
                    insertOrderProductsPreparedSt.executeUpdate();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void update(Order order) throws SQLException{
        // TODO: OrderProducts Update Note - orderProducts is immutable
        Order or = findById(order.getId());
        if(or == null){
            throw new RuntimeException("Errors occurred while find this object");
        }
        var orderStatus = order.getStatus().toString();
        updatePreparedSt.setString(1, orderStatus);
        updatePreparedSt.setLong(2, order.getId());
        updatePreparedSt.executeUpdate();
    }


    public void saveUserOrders(User user) throws SQLException{
        // Called when User already have id, but Order in user.orderList maybe not(saved locally, not in DB). Order in user.orderList already have orderProducts
        // TODO: UserOrders +completed
        var userOrders = user.getUserOrders();
        if(!userOrders.isEmpty()){
            userOrders.forEach((order)->{
                try {
                    if (!orderIsPresentInDB(order)){
                        save(new Order(user.getId(), order.getStatus()));
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private boolean orderProductIsPresentInDB(Product product) throws SQLException{
        PreparedStatement selectProductFromOrderProductsSt = con.prepareStatement("SELECT * FROM order_products WHERE products_id=?");
        selectProductFromOrderProductsSt.setLong(1, product.getId());
        if(selectProductFromOrderProductsSt.execute()){
            ResultSet rs = selectProductFromOrderProductsSt.getResultSet();
            return rs.next();
        }
        return false;
    }

    private boolean orderIsPresentInDB(Order order) throws SQLException{
        selectPreparedSt.setLong(1, order.getId());
        boolean hasResult = selectPreparedSt.execute();
        if(hasResult){
            ResultSet rs = selectPreparedSt.getResultSet();
            return rs.next();
        }
        return false;
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

    public List<Order> findOrdersByUser(User user) throws SQLException {
        List<Order> resultList = new ArrayList<>();
        PreparedStatement selectAllUserOrders = con.prepareStatement("SELECT * FROM orders WHERE users_id=?");
        selectAllUserOrders.setLong(1, user.getId());
        if (selectAllUserOrders.execute()){
            ResultSet rs = selectAllUserOrders.getResultSet();
            while (rs.next()){
                var orderId = rs.getLong("id");
                var userId = rs.getLong("users_id");
                var orderStatus = Order.OrderStatus.valueOf(rs.getString("status"));
                var order = new Order(orderId, userId, orderStatus);
                var orderProducts = getOrderProducts(order);
                order.setOrderProducts(orderProducts);
                resultList.add(order);
            }
        }
        return resultList;
    }

    private HashMap<Product, Long> getOrderProducts(Order order) throws SQLException {
        var productDao = new ProductDAO();
        HashMap<Product,Long> resultHashMap = new HashMap<>();
        PreparedStatement selectOrderProducts = con.prepareStatement("SELECT * FROM order_products WHERE orders_id=?");
        selectOrderProducts.setLong(1, order.getId());
        if (selectOrderProducts.execute()){
            ResultSet rs = selectOrderProducts.getResultSet();
            while (rs.next()){
                Long productsCount = rs.getLong("count");
                Long productId = rs.getLong("products_id");
                Product product = productDao.findById(productId);
                resultHashMap.put(product, productsCount);
            }
        }
        return resultHashMap;
    }
}
