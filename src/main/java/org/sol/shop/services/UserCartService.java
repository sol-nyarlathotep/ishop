package org.sol.shop.services;

import org.sol.shop.dal.ProductDAO;
import org.sol.shop.dal.UserDAO;
import org.sol.shop.models.Product;
import org.sol.shop.models.User;

import java.sql.SQLException;
import java.util.HashMap;

public class UserCartService implements IUserCartService{

    private final ProductDAO productDAO = new ProductDAO();
    private final UserDAO userDAO = new UserDAO();

    public UserCartService() throws SQLException {
    }

    @Override
    public void addProductToCart(User targetUser, Product product, Long count) {
        targetUser.addToCart(product, count);
        try {
            userDAO.update(targetUser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    @Override
//    public HashMap<Product, Long> loadUserCart(User user) {
//        userDAO.us
//        return null;
//    }

    @Override
    public void decreaseProductCount(User user, Product product, Long count) {
        user.removeFromCart(product, count);
        try {
            userDAO.update(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    @Override
//    public void increaseProductCount(User user, Product product, Long count) {
//        user.a
//    }
}
