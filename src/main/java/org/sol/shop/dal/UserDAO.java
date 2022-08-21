package org.sol.shop.dal;

import org.sol.shop.models.User;
import org.sol.shop.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAO implements IUserDAO{
    private Connection con = DBUtils.getConnection();
    private PreparedStatement insertPreparedSt = con.prepareStatement("INSERT INTO users (login, password, is_admin, blocked) VALUES (?,?,?,?)");
    private PreparedStatement selectPreparedSt = con.prepareStatement("SELECT * FROM users WHERE id=?");
    private PreparedStatement updatePreparedSt = con.prepareStatement("UPDATE users SET login=?, password=?, is_admin=?, blocked=? WHERE id=?");
    private PreparedStatement deletePreparedSt = con.prepareStatement("DELETE FROM users WHERE id=?");

    public UserDAO() throws SQLException {
    }

    @Override
    public User findById(Long id) throws SQLException {
        User user = null;
        selectPreparedSt.setLong(1, id);
        boolean hasResult = selectPreparedSt.execute();
        if (hasResult){
            ResultSet rs = selectPreparedSt.getResultSet();
            rs.next();
            String userLogin = rs.getString("login");
            String userPassword = rs.getString("password");
            Boolean userIsAdmin = rs.getBoolean("is_admin");
            Boolean userIsBlocked = rs.getBoolean("blocked");
            user = new User(id, userLogin, userPassword, userIsAdmin, userIsBlocked);
        }
        return user;
    }

    @Override
    public void save(User user) throws SQLException {
        var userLogin = user.getLogin();
        var userPwd = user.getPassword();
        var userIsAdmin = user.isAdmin();
        var userIsBlocked = user.isBlocked();

        insertPreparedSt.setString(1, userLogin);
        insertPreparedSt.setString(2, userPwd);
        insertPreparedSt.setBoolean(3, userIsAdmin);
        insertPreparedSt.setBoolean(4, userIsBlocked);
        insertPreparedSt.executeUpdate();
    }

    @Override
    public void update(User user) throws SQLException {
        User u = findById(user.getId());
        if (u == null){
            throw new RuntimeException("Errors occurred while find this object");
        }
        // login=?, password=?, is_admin=?, blocked=? WHERE id=?
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
}
