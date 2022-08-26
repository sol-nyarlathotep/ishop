package org.sol.shop.services;

import org.sol.shop.dal.ProductDAO;
import org.sol.shop.models.Product;
import org.sol.shop.models.User;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ProductService implements IProductService{

    ProductDAO productDAO = new ProductDAO();

    public ProductService() throws SQLException {
    }

    @Override
    public List<Product> loadCatalog() {
        try {
            return productDAO.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<Product> search(String searchQuery) {
        try {
            return new TreeSet<>(productDAO.searchSpecificByName(searchQuery));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void editDescription(User runAsUser, Product targetProduct, String newDescription) {
        if (runAsUser.isAdmin()){
            targetProduct.setDescription(newDescription);
            try {
                productDAO.update(targetProduct);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        throw new RuntimeException("You need to be an admin to change product description.");
    }

    @Override
    public void editName(User runAsUser, Product targetProduct, String newName) {
        if (runAsUser.isAdmin()){
            targetProduct.setName(newName);
            try {
                productDAO.update(targetProduct);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        throw new RuntimeException("You need to be an admin to change product name");
    }

    @Override
    public void editPrice(User runAsUser, Product targetProduct, BigDecimal newPrice){
        if (runAsUser.isAdmin()){
            targetProduct.setPrice(newPrice);
            try {
                productDAO.update(targetProduct);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        throw new RuntimeException("You need to be an admin to change product name");
    }

    @Override
    public void editStockCount(User runAsUser, Product targetProduct, Long newStockCount) {
        if(runAsUser.isAdmin()){
            targetProduct.setStockCount(newStockCount);
            try {
                productDAO.update(targetProduct);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("You need to be an admin to change stock count of product.");
    }

    @Override
    public void removeProductFromShop(User runAsUser, Product targetProduct) {
        if(runAsUser.isAdmin()){
            try {
                productDAO.delete(targetProduct);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        throw new RuntimeException("You need to be an admin to delete product from shop.");
    }

    @Override
    public void addNewProductToShop(User runAsUser, Product product) {
        if(runAsUser.isAdmin()){
            try {
                productDAO.save(product);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        throw new RuntimeException("You need to be an admin to add new product to shop.");
    }
}
