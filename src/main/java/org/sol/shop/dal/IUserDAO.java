package org.sol.shop.dal;

import org.sol.shop.models.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserDAO {
    User findByLogin(String login) throws SQLException;
    User findById(Long id) throws SQLException;
    void save(User user) throws SQLException;
    void update(User user) throws SQLException;
    void delete(User user) throws SQLException;
    List<User> findAll() throws SQLException;
}
