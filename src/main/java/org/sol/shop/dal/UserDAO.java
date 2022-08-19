package org.sol.shop.dal;

import org.sol.shop.models.User;

import java.util.List;

public interface UserDAO {

    User findById(Long id);
    void save(User user);
    void update(User user);
    void delete(User user);
    List<User> findAll();


}
