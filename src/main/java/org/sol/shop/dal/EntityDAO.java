package org.sol.shop.dal;

import org.sol.shop.models.Entity;

import java.sql.SQLException;
import java.util.List;

public interface EntityDAO {
    Entity findById(Long id) throws SQLException;
    void save(Entity e) throws SQLException;
    void update(Entity e) throws SQLException;
    void delete(Entity e) throws SQLException;
    List<Entity> findAll() throws SQLException;
}
