package org.sol.shop.services;

import org.sol.shop.models.Product;
import org.sol.shop.models.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface IProductService {
    List<Product> loadCatalog();
    Set<Product> search(String searchQuery);

    // Admins
    void editDescription(User runAsUser, Product targetProduct, String newDescription);
    void editName(User runAsUser, Product targetProduct, String newName);
    void editPrice(User runAsUser, Product targetProduct, BigDecimal newPrice);
    void editStockCount(User runAsUser, Product targetProduct, Long newStockCount);
    void removeProductFromShop(User runAsUser, Product targetProduct);
    void addNewProductToShop(User runAsUser, Product product);

}
