package org.sol.shop.dal;

import org.sol.shop.models.Product;

import java.util.List;

public interface ProductDAO {

    Product findById(Long id);
    void save(Product product);
    void update(Product product);
    void delete(Product product);
    List<Product> findAll();

}
