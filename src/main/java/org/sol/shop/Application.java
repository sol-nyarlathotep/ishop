package org.sol.shop;

import org.sol.shop.dal.OrderDAO;
import org.sol.shop.dal.ProductDAO;
import org.sol.shop.dal.UserDAO;
import org.sol.shop.models.Order;
import org.sol.shop.models.Product;
import org.sol.shop.models.User;
import org.sol.shop.utils.DBUtils;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;

public class Application {
    public static void init() throws SQLException {
        DBUtils.initDB();

        var udao = new UserDAO();
        var pdao = new ProductDAO();
        var odao = new OrderDAO();

        var user1 = new User("original_login", "superpassword", false, false);

        var meat = new Product("Meat", "Fresh MEAT!", new BigDecimal("9.42"), 10_000L);
        pdao.save(meat);
        var orange = new Product("Orange", "Good Orange!", new BigDecimal("3.41"), 20_000L);
        pdao.save(orange);

        user1.addToCart(meat, 4L);
        user1.addToCart(orange, 3L);

        udao.save(user1);

        user1.addToCart(meat, 3L);
        udao.update(user1);

        var u1cart = user1.getUserCart();
        var u1order = new Order(user1.getId(), Order.OrderStatus.PROCESSED, new HashMap<>(u1cart));
        user1.removeFromCart(meat, 4L);
        user1.removeFromCart(orange, 3L);
        odao.save(u1order);
        udao.update(user1);


//        DBUtils.destructDB();
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
