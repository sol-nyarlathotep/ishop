package org.sol.shop;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        Application.init();
//        try{
//            Connection c = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "sa");
//            var result = c.createStatement().executeUpdate("CREATE TABLE USERS(ID INT PRIMARY KEY, NAME VARCHAR(255))");
//
//            c.createStatement().execute("INSERT INTO USERS VALUES(1, 'Azathoth')");
//            Statement st = c.createStatement();
//            boolean res =  st.execute("SELECT * FROM USERS");
//            var rs = st.getResultSet();
//            System.out.println(rs);
//            }catch (SQLException e){
//            e.printStackTrace();
//        }
    }
}