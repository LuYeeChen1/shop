package com.backend.shop.Repository.Product;

import com.backend.shop.Model.Product.ProductModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * RowMapper: map one row to ProductModel
     */
    private ProductModel mapRowToProduct(ResultSet rs, int rowNum) throws SQLException {
        ProductModel p = new ProductModel();
        p.setProductId(rs.getLong("product_id"));
        p.setSellerUserId(rs.getLong("seller_user_id"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setStockQuantity(rs.getInt("stock_quantity"));
        p.setCategory(rs.getString("category"));
        p.setImageUrl(rs.getString("image_url"));
        p.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return p;
    }

    /**
     * Find all products by seller user id.
     */
    public List<ProductModel> findBySellerUserId(Long sellerUserId) {
        String sql = "SELECT * FROM product_table WHERE seller_user_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, this::mapRowToProduct, sellerUserId);
    }

    /**
     * Find one product by id.
     */
    public ProductModel findById(Long productId) {
        String sql = "SELECT * FROM product_table WHERE product_id = ?";
        List<ProductModel> list = jdbcTemplate.query(sql, this::mapRowToProduct, productId);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * Insert a new product.
     */
    public void save(ProductModel product) {
        String sql = """
                INSERT INTO product_table 
                (seller_user_id, name, description, price, stock_quantity, category, image_url, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, NOW())
                """;

        jdbcTemplate.update(
                sql,
                product.getSellerUserId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCategory(),
                product.getImageUrl()
        );
    }

    /**
     * Update an existing product.
     */
    public void update(ProductModel product) {
        String sql = """
                UPDATE product_table
                SET name = ?, 
                    description = ?, 
                    price = ?, 
                    stock_quantity = ?, 
                    category = ?, 
                    image_url = ?
                WHERE product_id = ?
                """;

        jdbcTemplate.update(
                sql,
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
     * Delete a product by id.
     */
    public void deleteProductById(Long productId) {
        String sql = "DELETE FROM product_table WHERE product_id = ?";
        jdbcTemplate.update(sql, productId);
    }
}
