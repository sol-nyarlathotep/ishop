package org.sol.shop.services;

import org.sol.shop.dal.UserDAO;
import org.sol.shop.models.Product;
import org.sol.shop.models.User;

import java.sql.SQLException;

public class UserService implements IUserService{

    private final UserDAO userDao = new UserDAO();

    public UserService() throws SQLException {
    }

    @Override
    public boolean loginUser(String login, String password) {
        try {
            User user = userDao.findByLogin(login);
            if (user == null){
                throw new RuntimeException("User with this login not found");
            }
            if(user.isBlocked()){
                throw new RuntimeException("User with this login is blocked");
            }
            //TODO: Implement
            authorizeUser(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean loginUser(User user, String authKey){
        //TODO: Implement.
        return false;
    }

    private void authorizeUser(User user){

    }

    @Override
    public boolean registerUser(String login, String password) {
        return false;
    }

//    @Override
//    public void addProductToCart(Product product) {
//
//    }

    @Override
    public boolean blockUser(User runAsUser, User userToBlock) {
        return false;
    }

    @Override
    public boolean unblockUser(User runAsUser, User userToUnblock) {
        return false;
    }
}
