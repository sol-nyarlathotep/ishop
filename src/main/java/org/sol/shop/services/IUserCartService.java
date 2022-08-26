package org.sol.shop.services;

import org.sol.shop.models.Product;
import org.sol.shop.models.User;

import java.util.HashMap;

public interface IUserCartService {
    void addProductToCart(User targetUser, Product product, Long count);
//    HashMap<Product, Long> loadUserCart(User user);
    void decreaseProductCount(User user, Product product, Long count);
//    void increaseProductCount(User user, Product product, Long count);
}
