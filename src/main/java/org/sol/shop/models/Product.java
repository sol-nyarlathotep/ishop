package org.sol.shop.models;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private Long id, stockCount;
    private String name, description;
    private BigDecimal price;

    // TODO: When Order has completed decrease the stockCount.

    public Product(Long id, Long stockCount, String name, String description, BigDecimal price) {
        this.id = id;
        this.stockCount = stockCount;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Product(String name, String description, BigDecimal price, Long stockCount) {
        this.stockCount = stockCount;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getId() {
        if (id == null){
            throw new RuntimeException("The product has not been saved to the database. ID is unavailable.");
        }
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStockCount() {
        return stockCount;
    }

    public void setStockCount(Long stockCount) {
        this.stockCount = stockCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return getId().equals(product.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
