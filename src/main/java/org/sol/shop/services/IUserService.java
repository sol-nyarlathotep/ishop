package org.sol.shop.services;

import org.sol.shop.models.Product;
import org.sol.shop.models.User;

public interface IUserService {
    boolean loginUser(String login, String password);
    boolean registerUser(String login, String password);
//    void addProductToCart(Product product);
    boolean blockUser(User runAsUser, User userToBlock);
    boolean unblockUser(User runAsUser, User userToUnblock);
}
