package org.sol.shop.dal;

import org.sol.shop.models.Order;
import org.sol.shop.models.Product;
import org.sol.shop.models.User;
import org.sol.shop.utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserDAO implements IUserDAO{
    private final Connection con = DBUtils.getConnection();
    private final PreparedStatement insertPreparedSt = con.prepareStatement("INSERT INTO users (login, password, is_admin, blocked) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
    private final PreparedStatement selectByIdPreparedSt = con.prepareStatement("SELECT * FROM users WHERE id=?");
    private final PreparedStatement selectByLoginPreparedSt = con.prepareStatement("SELECT * FROM users WHERE login=?");
    private final PreparedStatement updatePreparedSt = con.prepareStatement("UPDATE users SET login=?, password=?, is_admin=?, blocked=? WHERE id=?");
    private final PreparedStatement deletePreparedSt = con.prepareStatement("DELETE FROM users WHERE id=?");

    public UserDAO() throws SQLException {
    }


    @Override
    public User findByLogin(String login) throws SQLException {
        User user = null;
        selectByLoginPreparedSt.setString(1, login);
        boolean hasResult = selectByIdPreparedSt.execute();
        if(hasResult){
            ResultSet rs = selectByLoginPreparedSt.getResultSet();
            var usersList = deserializeUsers(rs);
            if(usersList.size() > 0){
                user = usersList.get(0);
            }
        }
        return user;
    }

    @Override
    public User findById(Long id) throws SQLException {
        User user = null;
        selectByIdPreparedSt.setLong(1, id);
        boolean hasResult = selectByIdPreparedSt.execute();
        if (hasResult){
            ResultSet rs = selectByIdPreparedSt.getResultSet();
            var usersList = deserializeUsers(rs);
            if(usersList.size() > 0)
                user = usersList.get(0);
        }
        return user;
    }

    @Override
    public void save(User user) throws SQLException {
        var orderDao = new OrderDAO();
        {
            // TODO: SAVE ORDERS IN METHOD saveUserOrders +completed
            var userLogin = user.getLogin();
            var userPwd = user.getPassword();
            var userIsAdmin = user.isAdmin();
            var userIsBlocked = user.isBlocked();
            insertPreparedSt.setString(1, userLogin);
            insertPreparedSt.setString(2, userPwd);
            insertPreparedSt.setBoolean(3, userIsAdmin);
            insertPreparedSt.setBoolean(4, userIsBlocked);
            insertPreparedSt.execute();
        }
        var rs = insertPreparedSt.getGeneratedKeys();
        rs.next();
        user.setId(rs.getLong(1));
        saveUserCart(user);
        orderDao.saveUserOrders(user);
    }

    private void serializeUser(User user) throws SQLException {
        // TODO: ? Serialize User ?
    }

    @Override
    public void update(User user) throws SQLException {
        // TODO: UPDATE: Add new order in method saveUserOrders +completed
        User u = findById(user.getId());
        if (u == null){
            throw new RuntimeException("Errors occurred while find this object");
        }
        var orderDao = new OrderDAO();
        {
            var userLogin = user.getLogin();
            var userPassword = user.getPassword();
            var userIsAdmin = user.isAdmin();
            var userIsBlocked = user.isBlocked();
            updatePreparedSt.setLong(5, user.getId());
            updatePreparedSt.setString(1, userLogin);
            updatePreparedSt.setString(2, userPassword);
            updatePreparedSt.setBoolean(3, userIsAdmin);
            updatePreparedSt.setBoolean(4, userIsBlocked);
            updatePreparedSt.executeUpdate();
        }
        saveUserCart(user);
        orderDao.saveUserOrders(user);
    }

    private void saveUserCart(User user) throws SQLException{
        //TODO: BUG: When delete from local-cart it still present in DB; Iterate over ResultSet from cart_products -> if specific product are not in user-local-cart then full remove product from DB +completed;
        var userCart = user.getUserCart();
        if(!userCart.isEmpty()){
            userCart.keySet().forEach((product -> {
                Long productCount = userCart.get(product);
                try {
                    Long productCountInDB = getCountOfProductInCart(user, product);
                    if(!isProductPresentInCart(user, product)){
                        addProductToCart(user, product, productCount);
                    }else if(productCount > productCountInDB){
                        addProductToCart(user, product, productCount-productCountInDB);
                    }else if(productCount < productCountInDB){
                        removeSpecificCountOfProductFromCart(user, product, productCountInDB - productCount);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }));
            var dbUserCart = getUserCart(user);
            dbUserCart.forEach((product, count)->{
                if (!userCart.containsKey(product)){
                    try {
                        removeProductFromCart(user, product);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } else {
            cleanUserCartInDB(user);
        }
    }


    private void cleanUserCartInDB(User user) throws SQLException {
        PreparedStatement fullRemoveUserCartEntries = con.prepareStatement("DELETE FROM cart_products WHERE users_id=?");
        fullRemoveUserCartEntries.setLong(1, user.getId());
        fullRemoveUserCartEntries.executeUpdate();
    }

    @Override
    public void delete(User user) throws SQLException {
        User u = findById(user.getId());
        if (u == null){
            throw new RuntimeException("Errors occurred while find this object");
        }
        deletePreparedSt.setLong(1, user.getId());
        deletePreparedSt.executeUpdate();
    }

    @Override
    public List<User> findAll() throws SQLException {
        // TODO: Implement method.
        return null;
    }

    private void addProductToCart(User user, Product product, Long count) throws SQLException {
        PreparedStatement insertNewProductEntry = con.prepareStatement("INSERT INTO cart_products (users_id, products_id, count) VALUES (?,?,?)");
        PreparedStatement updateExistedProductEntry = con.prepareStatement("UPDATE cart_products SET count=? WHERE users_id=? AND products_id=?");
        insertNewProductEntry.setLong(1, user.getId());
        insertNewProductEntry.setLong(2, product.getId());
        updateExistedProductEntry.setLong(2, user.getId());
        updateExistedProductEntry.setLong(3, product.getId());
        if (!isProductPresentInCart(user, product)){
            insertNewProductEntry.setLong(3, count);
            insertNewProductEntry.executeUpdate();
            return;
        }
        // TODO: Update +completed;
        updateExistedProductEntry.setLong(1, getCountOfProductInCart(user, product)+count);
        updateExistedProductEntry.executeUpdate();
    }

    private void removeSpecificCountOfProductFromCart(User user, Product product, Long count) throws SQLException{
        PreparedStatement fullRemoveSt = con.prepareStatement("DELETE FROM cart_products WHERE products_id=? AND users_id=?");
        PreparedStatement removeSpecificCountSt = con.prepareStatement("UPDATE cart_products SET count=? WHERE products_id=? AND users_id=?");
        if(!isProductPresentInCart(user, product)){
            throw new RuntimeException("Not found product to delete");
        }
        if(count < 1){
            throw new RuntimeException("You can't delete 0 or negative count of product");
        }
        if(count > getCountOfProductInCart(user, product)){
            throw new RuntimeException("You cannot delete more than what is in the cart");
        }
        if (count.equals(getCountOfProductInCart(user, product))){
            fullRemoveSt.setLong(1, product.getId());
            fullRemoveSt.setLong(2, user.getId());
            fullRemoveSt.executeUpdate();
            return;
        }
        Long productCountInDB = getCountOfProductInCart(user, product);
        removeSpecificCountSt.setLong(1, productCountInDB - count);
        removeSpecificCountSt.setLong(2, product.getId());
        removeSpecificCountSt.setLong(3, user.getId());
        removeSpecificCountSt.executeUpdate();
    }

    private void removeProductFromCart(User user, Product product) throws SQLException{
        PreparedStatement removeProductSt = con.prepareStatement("DELETE FROM cart_products WHERE products_id=? AND users_id=?");
        removeProductSt.setLong(1, product.getId());
        removeProductSt.setLong(2, user.getId());
        removeProductSt.executeUpdate();
    }

    private boolean isProductPresentInCart(User user, Product product) throws SQLException{
        return getCountOfProductInCart(user, product) != 0;
    }

    private Long getCountOfProductInCart(User user, Product product) throws SQLException{
        // TODO: repair bug: insert users_id to pst +completed;
        PreparedStatement st = con.prepareStatement("SELECT * FROM cart_products WHERE products_id=? AND users_id=?");
        st.setLong(1, product.getId());
        st.setLong(2, user.getId());
        if(st.execute()){
            ResultSet rs = st.getResultSet();
            if(rs.next()){
                return rs.getLong("count");
            }
            return 0L;
        }
        throw new RuntimeException("Errors occurred during execute query");
    }

    private HashMap<Product, Long> getUserCart(User user) throws SQLException {
        ProductDAO productDAO = new ProductDAO();
        HashMap<Product, Long> userCart = new HashMap<>();
        PreparedStatement st = con.prepareStatement("SELECT * FROM cart_products WHERE users_id=?");
        st.setLong(1, user.getId());
        if(st.execute()){
            ResultSet rs = st.getResultSet();
            while (rs.next()){
                Long productId = rs.getLong("products_id");
                Long count = rs.getLong("count");
                userCart.put(productDAO.findById(productId), count);
            }
            return userCart;
        }
        throw new RuntimeException("Errors occurred during ex query");
    }



    private List<User> deserializeUsers(ResultSet rs) throws SQLException {
        var orderDao = new OrderDAO();
        var resultList = new ArrayList<User>();
        while (rs.next()){
            // TODO: deserialize User orders. +completed
            String userLogin = rs.getString("login");
            String userPassword = rs.getString("password");
            boolean userIsAdmin = rs.getBoolean("is_admin");
            boolean userIsBlocked = rs.getBoolean("blocked");
            Long userId = rs.getLong("id");

            var user = new User(userId, userLogin, userPassword, userIsAdmin, userIsBlocked);
            // Cart
            HashMap<Product, Long> userCart = getUserCart(user);
            user.setUserCart(userCart);


            // Orders
            List<Order> userOrders = orderDao.findOrdersByUser(user);
            user.setUserOrders(userOrders);


            // Add To List
            resultList.add(user);
        }
        return resultList;
    }

}
