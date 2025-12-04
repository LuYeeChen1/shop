package com.backend.shop.Repository.Product;

import com.backend.shop.Model.Product.ProductModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * RowMapper for ProductModel.
     * Maps one row from product_table to a ProductModel object.
     */
    private ProductModel mapRowToProduct(ResultSet rs, int rowNum) throws SQLException {
        ProductModel p = new ProductModel();

        p.setProductId(rs.getLong("product_id"));
        p.setSellerUserId(rs.getLong("seller_user_id"));

        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));

        BigDecimal price = rs.getBigDecimal("price");
        p.setPrice(price);

        int stockQty = rs.getInt("stock_quantity");
        if (rs.wasNull()) {
            p.setStockQuantity(null);
        } else {
            p.setStockQuantity(stockQty);
        }

        p.setCategory(rs.getString("category"));
        p.setImageUrl(rs.getString("image_url"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            p.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            p.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return p;
    }

    /**
     * Insert a new product.
     * This method does not return generated product_id.
     */
    public void save(ProductModel product) {
        String sql = """
            INSERT INTO product_table (
                seller_user_id,
                name,
                description,
                price,
                stock_quantity,
                category,
                image_url,
                created_at,
                updated_at
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createdAt = product.getCreatedAt() != null ? product.getCreatedAt() : now;
        LocalDateTime updatedAt = product.getUpdatedAt() != null ? product.getUpdatedAt() : now;

        jdbcTemplate.update(sql,
                product.getSellerUserId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCategory(),
                product.getImageUrl(),
                createdAt,
                updatedAt
        );
    }

    /**
     * Update an existing product by product_id.
     * Does not allow changing seller_user_id.
     */
    public void update(ProductModel product) {
        String sql = """
            UPDATE product_table SET
                name = ?,
                description = ?,
                price = ?,
                stock_quantity = ?,
                category = ?,
                image_url = ?,
                updated_at = NOW()
            WHERE product_id = ?
            """;

        jdbcTemplate.update(sql,
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCategory(),
                product.getImageUrl(),
                product.getProductId()
        );
    }

    /**
     * Find a product by its id.
     * Returns null if not found.
     */
    public ProductModel findById(Long productId) {
        String sql = "SELECT * FROM product_table WHERE product_id = ?";

        List<ProductModel> list = jdbcTemplate.query(sql, this::mapRowToProduct, productId);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * Find all products for a given seller.
     */
    public List<ProductModel> findBySellerUserId(Long sellerUserId) {
        String sql = "SELECT * FROM product_table WHERE seller_user_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, this::mapRowToProduct, sellerUserId);
    }

    /**
     * Find all products in the system.
     */
    public List<ProductModel> findAll() {
        String sql = "SELECT * FROM product_table ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, this::mapRowToProduct);
    }

    /**
     * Delete a product by its id.
     */
    public void deleteById(Long productId) {
        String sql = "DELETE FROM product_table WHERE product_id = ?";
        jdbcTemplate.update(sql, productId);
    }
}
