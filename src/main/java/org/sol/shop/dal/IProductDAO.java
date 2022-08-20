package org.sol.shop.dal;

import org.sol.shop.models.Product;

import java.sql.SQLException;
import java.util.List;

public interface IProductDAO {
    Product findById(Long id) throws SQLException;
    void save(Product product) throws SQLException;
    void update(Product product) throws SQLException;
    void delete(Product product) throws SQLException;
    List<Product> findAll() throws SQLException;
}
