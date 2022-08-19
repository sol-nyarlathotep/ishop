package org.sol.shop;

import org.sol.shop.utils.DBUtils;

import java.sql.SQLException;

public class Application {
    public static void init() throws SQLException {
        DBUtils.initDB();
        System.out.println("test");
    }
}
