package com.backend.shop.Repository.Cart;

import com.backend.shop.Model.Cart.CartItemModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CartRepository {

    private final JdbcTemplate jdbcTemplate;

    public CartRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * RowMapper: map one row to CartItemModel.
     */
    private CartItemModel mapRowToCartItem(ResultSet rs, int rowNum) throws SQLException {
        CartItemModel item = new CartItemModel();
        item.setUserId(rs.getLong("user_id"));
        item.setProductId(rs.getLong("product_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setAddedAt(rs.getTimestamp("added_at").toLocalDateTime());
        return item;
    }

    /**
     * Find all cart items for a given user.
     */
    public List<CartItemModel> findByUserId(Long userId) {
        String sql = "SELECT * FROM cart_items WHERE user_id = ? ORDER BY added_at DESC";
        return jdbcTemplate.query(sql, this::mapRowToCartItem, userId);
    }

    /**
     * Find one cart item by user and product.
     */
    public CartItemModel findByUserIdAndProductId(Long userId, Long productId) {
        String sql = "SELECT * FROM cart_items WHERE user_id = ? AND product_id = ?";
        List<CartItemModel> list = jdbcTemplate.query(sql, this::mapRowToCartItem, userId, productId);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * Insert a new cart item.
     */
    public void insert(CartItemModel item) {
        String sql = """
                INSERT INTO cart_items (user_id, product_id, quantity, added_at)
                VALUES (?, ?, ?, NOW())
                """;

        jdbcTemplate.update(
                sql,
                item.getUserId(),
                item.getProductId(),
                item.getQuantity()
        );
    }

    /**
     * Update quantity for an existing cart item.
     */
    public void updateQuantity(Long userId, Long productId, Integer quantity) {
        String sql = """
                UPDATE cart_items
                SET quantity = ?
                WHERE user_id = ? AND product_id = ?
                """;

        jdbcTemplate.update(sql, quantity, userId, productId);
    }

    /**
     * Delete a single item from cart.
     */
    public void deleteItem(Long userId, Long productId) {
        String sql = "DELETE FROM cart_items WHERE user_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, userId, productId);
    }

    /**
     * Clear all items in a user's cart.
     */
    public void clearCart(Long userId) {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }
}
