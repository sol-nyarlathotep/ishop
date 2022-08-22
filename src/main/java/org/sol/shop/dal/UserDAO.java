package org.sol.shop.dal;

import org.sol.shop.models.Product;
import org.sol.shop.models.User;
import org.sol.shop.utils.DBUtils;

import java.sql.*;
import java.util.HashMap;
import java.util.List;

public class UserDAO implements IUserDAO{
    private final Connection con = DBUtils.getConnection();
    private final PreparedStatement insertPreparedSt = con.prepareStatement("INSERT INTO users (login, password, is_admin, blocked) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
    private final PreparedStatement selectPreparedSt = con.prepareStatement("SELECT * FROM users WHERE id=?");
    private final PreparedStatement updatePreparedSt = con.prepareStatement("UPDATE users SET login=?, password=?, is_admin=?, blocked=? WHERE id=?");
    private final PreparedStatement deletePreparedSt = con.prepareStatement("DELETE FROM users WHERE id=?");

    public UserDAO() throws SQLException {
    }


    @Override
    public User findByLogin(String login) throws SQLException {
        //TODO: Implement
        return null;
    }

    @Override
    public User findById(Long id) throws SQLException {
        User user = null;
        selectPreparedSt.setLong(1, id);
        boolean hasResult = selectPreparedSt.execute();
        if (hasResult){
            ResultSet rs = selectPreparedSt.getResultSet();
            if(rs.next()){
                String userLogin = rs.getString("login");
                String userPassword = rs.getString("password");
                boolean userIsAdmin = rs.getBoolean("is_admin");
                boolean userIsBlocked = rs.getBoolean("blocked");
                // TODO: get User Cart From DB
                user = new User(id, userLogin, userPassword, userIsAdmin, userIsBlocked);
                HashMap<Product, Long> userCart = getUserCart(user);
                user.setUserCart(userCart);
            }
        }
        return user;
    }

    @Override
    public void save(User user) throws SQLException {
        {
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
    }

    @Override
    public void update(User user) throws SQLException {
        User u = findById(user.getId());
        if (u == null){
            throw new RuntimeException("Errors occurred while find this object");
        }
        // login=?, password=?, is_admin=?, blocked=? WHERE id=?
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
    }

    private void saveUserCart(User user) throws SQLException{
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
                        removeProductFromCart(user, product, productCountInDB - productCount);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }));
        }
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
        return null;
    }

    private void addProductToCart(User user, Product product, Long count) throws SQLException {
        PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO cart_products (users_id, products_id, count) VALUES (?,?,?)");
        preparedStatement.setLong(1, user.getId());
        preparedStatement.setLong(2, product.getId());
        if (!isProductPresentInCart(user, product)){
            preparedStatement.setLong(3, count);
            preparedStatement.executeUpdate();
            return;
        }
        preparedStatement.setLong(3, getCountOfProductInCart(user, product)+count);
        preparedStatement.executeUpdate();
    }

    private void removeProductFromCart(User user, Product product, Long count) throws SQLException{
        PreparedStatement fullRemoveSt = con.prepareStatement("DELETE FROM cart_products WHERE products_id=? AND users_id=?");
        PreparedStatement removeSpecificCountSt = con.prepareStatement("UPDATE cart_products SET count=? WHERE products_id=? AND users_d=?");
        if(!isProductPresentInCart(user, product)){
            throw new RuntimeException("Not found product to delete");
        }
        if(count < 1){
            throw new RuntimeException("You can't delete 0 or negative count of product");
        }
        if(count > getCountOfProductInCart(user, product)){
            throw new RuntimeException("You cannot delete more than what is in the cart");
        }
        if (count == getCountOfProductInCart(user, product)){
            fullRemoveSt.setLong(1, product.getId());
            fullRemoveSt.setLong(2, user.getId());
            fullRemoveSt.executeUpdate();
            return;
        }
        removeSpecificCountSt.setLong(1, count);
        removeSpecificCountSt.setLong(2, product.getId());
        removeSpecificCountSt.setLong(3, user.getId());
        removeSpecificCountSt.executeUpdate();
    }

    private boolean isProductPresentInCart(User user, Product product) throws SQLException{
        return getCountOfProductInCart(user, product) != 0;
    }

    private Long getCountOfProductInCart(User user, Product product) throws SQLException{
        PreparedStatement st = con.prepareStatement("SELECT * FROM cart_products WHERE products_id=?");
        st.setLong(1, product.getId());
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

//    public List<Product> findAllUserProductsInCart(User user) throws SQLException{
//        PreparedStatement st = con.prepareStatement("SELECT * FROM cart_products WHERE proid=?");
//        st.setLong(1, user.getId());
//        boolean hasResult = st.execute();
//        if(hasResult){
//            var rs = st.getResultSet();
//            while (rs.next()){
//                Long productId = rs.getLong("product")
//            }
//        }
//    }
}
