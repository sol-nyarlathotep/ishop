package org.sol.shop.dal;

import org.sol.shop.models.Product;
import org.sol.shop.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ProductDAO implements IProductDAO{

    Connection con = DBUtils.getConnection();
    private PreparedStatement insertPreparedSt = con.prepareStatement("INSERT INTO products (price, name, description, stock_count) VALUES(?,?,?,?)");
    private PreparedStatement selectPreparedSt = con.prepareStatement("SELECT * FROM products WHERE id=?");
    private PreparedStatement updatePreparedSt = con.prepareStatement("UPDATE products SET price=?, name=?, description=?, stock_count=? WHERE id=?");
    private PreparedStatement deletePreparedSt = con.prepareStatement("DELETE FROM products WHERE id=?");

    public ProductDAO() throws SQLException {
    }

    @Override
    public Product findById(Long id) {
        return null;
    }

    @Override
    public void save(Product product) {

    }

    @Override
    public void update(Product product) {

    }

    @Override
    public void delete(Product product) {

    }

    @Override
    public List<Product> findAll() {
        return null;
    }
}
