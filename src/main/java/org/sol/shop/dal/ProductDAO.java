package org.sol.shop.dal;

import org.sol.shop.models.Product;
import org.sol.shop.utils.DBUtils;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

public class ProductDAO implements IProductDAO{

    Connection con = DBUtils.getConnection();
    private PreparedStatement insertPreparedSt = con.prepareStatement("INSERT INTO products (price, name, description, stock_count) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
    private PreparedStatement selectPreparedSt = con.prepareStatement("SELECT * FROM products WHERE id=?");
    private PreparedStatement updatePreparedSt = con.prepareStatement("UPDATE products SET price=?, name=?, description=?, stock_count=? WHERE id=?");
    private PreparedStatement deletePreparedSt = con.prepareStatement("DELETE FROM products WHERE id=?");

    public ProductDAO() throws SQLException {
    }

    @Override
    public Product findById(Long id) throws SQLException {
        Product product = null;
        selectPreparedSt.setLong(1, id);
        boolean hasResult = selectPreparedSt.execute();
        if(hasResult){
            ResultSet rs = selectPreparedSt.getResultSet();
            if(rs.next()){
                BigDecimal productPrice = rs.getBigDecimal("price");
                String productName = rs.getString("name");
                String productDescription = rs.getString("description");
                Long productStockCount = rs.getLong("stock_count");
                product = new Product(id, productStockCount, productName, productDescription, productPrice);
            }
        }
        return product;
    }

    @Override
    public void save(Product product) throws SQLException {
        var productPrice = product.getPrice();
        var productName = product.getName();
        var productDescription = product.getDescription();
        var productStockCount = product.getStockCount();
        insertPreparedSt.setBigDecimal(1, productPrice);
        insertPreparedSt.setString(2, productName);
        insertPreparedSt.setString(3, productDescription);
        insertPreparedSt.setLong(4, productStockCount);

        insertPreparedSt.execute();
        var rs = insertPreparedSt.getGeneratedKeys();
        rs.next();
        product.setId(rs.getLong(1));
    }

    @Override
    public void update(Product product) throws SQLException {
        Product pr = findById(product.getId());
        if (pr == null){
            throw new RuntimeException("Errors occurred while find this object");
        }
        // price=1, name=2, description=3, stock_count=4
        updatePreparedSt.setLong(5, product.getId()); // ID
        updatePreparedSt.setBigDecimal(1, product.getPrice()); // price
        updatePreparedSt.setString(2, product.getName()); // name
        updatePreparedSt.setString(3, product.getDescription()); // Desc
        updatePreparedSt.setLong(4, product.getStockCount());
        updatePreparedSt.executeUpdate();
    }

    @Override
    public void delete(Product product) throws SQLException {
        Product pr = findById(product.getId());
        if(pr == null){
            throw new RuntimeException("Errors occurred while find this object");
        }
        deletePreparedSt.setLong(1, product.getId());
        deletePreparedSt.executeUpdate();
    }

    @Override
    public List<Product> findAll() throws SQLException {
        return null;
    }
}
