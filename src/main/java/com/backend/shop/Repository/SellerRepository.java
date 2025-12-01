package com.backend.shop.Repository;

import com.backend.shop.Model.Seller.SellerModel;
import com.backend.shop.Model.Seller.SellerStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class SellerRepository {

    private final JdbcTemplate jdbcTemplate;

    public SellerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Row mapper for SellerModel.
     * Converts a database row into a SellerModel object.
     */
    private SellerModel mapRowToSeller(ResultSet rs, int rowNum) throws SQLException {
        SellerModel seller = new SellerModel();
        seller.setUserId(rs.getLong("user_id"));
        seller.setShopName(rs.getString("shop_name"));
        seller.setShopDescription(rs.getString("shop_description"));
        seller.setShopLogoUrl(rs.getString("shop_logo_url"));
        seller.setBusinessRegistrationNumber(rs.getString("business_registration_number"));
        seller.setBusinessAddress(rs.getString("business_address"));
        seller.setContactNumber(rs.getString("contact_number"));

        String statusString = rs.getString("status");
        seller.setStatus(statusString != null ? SellerStatus.valueOf(statusString) : null);

        Timestamp applied = rs.getTimestamp("applied_at");
        seller.setAppliedAt(applied != null ? applied.toLocalDateTime() : null);

        Timestamp reviewed = rs.getTimestamp("reviewed_at");
        seller.setReviewedAt(reviewed != null ? reviewed.toLocalDateTime() : null);

        seller.setReviewedByAdmin(rs.getString("reviewed_by_admin"));
        seller.setReviewComment(rs.getString("review_comment"));

        return seller;
    }

    /**
     * Insert new seller application.
     */
    public void save(SellerModel seller) {
        String sql = "INSERT INTO seller_table (" +
                "user_id, shop_name, shop_description, shop_logo_url, " +
                "business_registration_number, business_address, contact_number, " +
                "status, applied_at, reviewed_at, reviewed_by_admin, review_comment" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                seller.getUserId(),
                seller.getShopName(),
                seller.getShopDescription(),
                seller.getShopLogoUrl(),
                seller.getBusinessRegistrationNumber(),
                seller.getBusinessAddress(),
                seller.getContactNumber(),
                seller.getStatus() != null ? seller.getStatus().name() : null,
                seller.getAppliedAt() != null ? Timestamp.valueOf(seller.getAppliedAt()) : null,
                seller.getReviewedAt() != null ? Timestamp.valueOf(seller.getReviewedAt()) : null,
                seller.getReviewedByAdmin(),
                seller.getReviewComment()
        );
    }

    /**
     * Update seller application (mainly status, review info).
     */
    public void update(SellerModel seller) {
        String sql = "UPDATE seller_table SET " +
                "shop_name = ?, " +
                "shop_description = ?, " +
                "shop_logo_url = ?, " +
                "business_registration_number = ?, " +
                "business_address = ?, " +
                "contact_number = ?, " +
                "status = ?, " +
                "applied_at = ?, " +
                "reviewed_at = ?, " +
                "reviewed_by_admin = ?, " +
                "review_comment = ? " +
                "WHERE user_id = ?";

        jdbcTemplate.update(sql,
                seller.getShopName(),
                seller.getShopDescription(),
                seller.getShopLogoUrl(),
                seller.getBusinessRegistrationNumber(),
                seller.getBusinessAddress(),
                seller.getContactNumber(),
                seller.getStatus() != null ? seller.getStatus().name() : null,
                seller.getAppliedAt() != null ? Timestamp.valueOf(seller.getAppliedAt()) : null,
                seller.getReviewedAt() != null ? Timestamp.valueOf(seller.getReviewedAt()) : null,
                seller.getReviewedByAdmin(),
                seller.getReviewComment(),
                seller.getUserId()
        );
    }

    /**
     * Find seller by user_id.
     */
    public SellerModel findByUserId(Long userId) {
        String sql = "SELECT * FROM seller_table WHERE user_id = ?";

        List<SellerModel> list = jdbcTemplate.query(sql, this::mapRowToSeller, userId);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * Check if seller application already exists.
     */
    public boolean existsByUserId(Long userId) {
        String sql = "SELECT COUNT(*) FROM seller_table WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }

    /**
     * Get all sellers.
     */
    public List<SellerModel> findAll() {
        String sql = "SELECT * FROM seller_table";
        return jdbcTemplate.query(sql, this::mapRowToSeller);
    }

    /**
     * Get all sellers by status.
     */
    public List<SellerModel> findByStatus(SellerStatus status) {
        String sql = "SELECT * FROM seller_table WHERE status = ?";
        return jdbcTemplate.query(sql, this::mapRowToSeller, status.name());
    }

    /**
     * Delete seller by user_id.
     */
    public void deleteByUserId(Long userId) {
        String sql = "DELETE FROM seller_table WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }
}
