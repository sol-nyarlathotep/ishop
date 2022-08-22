package org.sol.shop;

import org.sol.shop.dal.ProductDAO;
import org.sol.shop.dal.UserDAO;
import org.sol.shop.models.Product;
import org.sol.shop.models.User;
import org.sol.shop.utils.DBUtils;

import java.math.BigDecimal;
import java.sql.SQLException;

public class Application {
    public static void init() throws SQLException {
        DBUtils.initDB();
        /*
        *
        * var userService = new UserService();
        * var productService = new ProductService();
        * var orderService = new OrderService();
        *
        *
        *
        *
        *
        *
        *
        * */
//        var userDao = new UserDAO();
//        var productDao = new ProductDAO();
//        User u = new User("ppe", "1234", false, false);
//        userDao.save(u);
//        Product meat = new Product("Meat", "Fresh Meat!", new BigDecimal("9.60"), 10_000L);
//        productDao.save(meat);
//        u.addToCart(meat, 3L);
//        u.addToCart(meat, 7L);
//        userDao.update(u);
//        User u1 = userDao.findById(1L);
//        var hm = u1.getUserCart();
//        System.out.println(hm);
//        userDao.findById(2L);
//        u.setAdmin(true);
//        userDao.update(u);
    }
}
