package org.sol.shop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Application {

    private static final String DB_LOGIN = "sa";
    private static final String DB_PASSWD = "sa";
    private static Connection connection;

    public static void init(){
        if(checkConnectionToDB()){
            try {
                connection = getConnection();
                checkDBTables();
            }catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean checkConnectionToDB() {
        try {
            DriverManager.getConnection("jdbc:h2:mem:test", DB_LOGIN, DB_PASSWD);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void checkDBTables() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM ?");
        ps.setString(1, "USERS");
        ps.addBatch();
        ps.executeBatch();
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:mem:test", DB_LOGIN, DB_PASSWD);
    }

}
