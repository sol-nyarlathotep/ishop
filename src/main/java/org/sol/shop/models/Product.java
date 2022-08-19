package org.sol.shop.models;

import java.math.BigDecimal;

public class Product {

    private Long id, stockCount;
    private String name, description;
    private BigDecimal price;

    public Product(Long id, Long stockCount, String name, String description, BigDecimal price) {
        this.id = id;
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

    public void setStockCount(Long stockCount) {
        this.stockCount = stockCount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public Long getStockCount() {
        return stockCount;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
