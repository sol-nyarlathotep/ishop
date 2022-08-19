package org.sol.shop.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {
    public static void initDB() throws SQLException{
        try(Connection con = getConnection()) {
            Statement st = con.createStatement();
            // TODO: Refactor
            st.addBatch("CREATE TABLE IF NOT EXISTS `products` (\n" +
                    "  `id` bigint AUTO_INCREMENT NOT NULL,\n" +
                    "  `price` bigint NOT NULL,\n" +
                    "  `name` text NOT NULL,\n" +
                    "  `description` text NOT NULL,\n" +
                    "  `stock_count` bigint NOT NULL,\n" +
                    "  PRIMARY KEY (`id`)\n" +
                    ")");
            st.addBatch("CREATE TABLE IF NOT EXISTS `users` (\n" +
                    "  `id` bigint AUTO_INCREMENT NOT NULL,\n" +
                    "  `login` varchar(40) NOT NULL,\n" +
                    "  `password` text NOT NULL,\n" +
                    "  `is_admin` boolean NOT NULL,\n" +
                    "  `blocked` boolean NOT NULL,\n" +
                    "  PRIMARY KEY (`id`)\n" +
                    ")");
            st.addBatch("CREATE TABLE IF NOT EXISTS `cart_products` (\n" +
                    "  `users_id` bigint NOT NULL,\n" +
                    "  `products_id` bigint NOT NULL,\n" +
                    "  `count` bigint NOT NULL,\n" +
                    "  CONSTRAINT `cart_products_users_id_users_id_foreign` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`),\n" +
                    "  CONSTRAINT `cart_products_products_id_products_id_foreign` FOREIGN KEY (`products_id`) REFERENCES `products` (`id`)\n" +
                    ")");
            st.addBatch("CREATE TABLE IF NOT EXISTS `orders` (\n" +
                    "  `users_id` bigint NOT NULL,\n" +
                    "  `id` bigint AUTO_INCREMENT NOT NULL,\n" +
                    "  `status` text NOT NULL,\n" +
                    "  PRIMARY KEY (`id`),\n" +
                    "  CONSTRAINT `table3_users_eenrfs8f3_foreign` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`)\n" +
                    ")");
            st.addBatch("CREATE TABLE IF NOT EXISTS `order_products` (\n" +
                    "  `orders_id` bigint NOT NULL,\n" +
                    "  `count` bigint NOT NULL,\n" +
                    "  `products_id` bigint NOT NULL,\n" +
                    "  CONSTRAINT `table3_orders_8j3yniybv_foreign` FOREIGN KEY (`orders_id`) REFERENCES `orders` (`id`),\n" +
                    "  CONSTRAINT `order_products_products_id_products_id_foreign` FOREIGN KEY (`products_id`) REFERENCES `products` (`id`)\n" +
                    ")");
            st.executeBatch();
        }
    }

    public static Connection getConnection() {
        HikariConfig config = new HikariConfig();
        config.setUsername("sa");
        config.setPassword("sa");
        config.setJdbcUrl("jdbc:h2:mem:test");
        Connection con = null;
        try {
            con = new HikariDataSource(config).getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return con;
    }
}
