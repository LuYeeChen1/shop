package com.backend.shop.Repository.Seller;

import com.backend.shop.Model.Seller.SellerModel;
import com.backend.shop.Model.Seller.SellerStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class SellerRepository {

    private final JdbcTemplate jdbcTemplate;

    public SellerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Row mapper for SellerModel.
     * Maps one row from the "sellers" table into a SellerModel object.
     */
    private SellerModel mapRowToSeller(ResultSet rs, int rowNum) throws SQLException {
        SellerModel s = new SellerModel();

        s.setUserId(rs.getLong("user_id"));

        // New: map email from database
        try {
            s.setEmail(rs.getString("email"));
        } catch (SQLException ex) {
            // If the column does not exist, ignore.
            // Make sure your table has an "email" column.
        }

        s.setShopName(rs.getString("shop_name"));
        s.setShopDescription(rs.getString("shop_description"));
        s.setShopLogoUrl(rs.getString("shop_logo_url"));

        // Match SellerModel field name: business_registration_number
        s.setBusinessRegistrationNumber(rs.getString("business_registration_number"));
        s.setBusinessAddress(rs.getString("business_address"));
        s.setContactNumber(rs.getString("contact_number"));

        // Convert VARCHAR/ENUM → Enum
        String statusStr = rs.getString("status");
        if (statusStr != null) {
            try {
                s.setStatus(SellerStatus.valueOf(statusStr));
            } catch (IllegalArgumentException ex) {
                // If database has invalid status value, keep it null
                s.setStatus(null);
            }
        }

        // Map LocalDateTime fields
        Timestamp appliedAt = rs.getTimestamp("applied_at");
        if (appliedAt != null) {
            s.setAppliedAt(appliedAt.toLocalDateTime());
        }

        Timestamp reviewedAt = rs.getTimestamp("reviewed_at");
        if (reviewedAt != null) {
            s.setReviewedAt(reviewedAt.toLocalDateTime());
        }

        s.setReviewedByAdmin(rs.getString("reviewed_by_admin"));
        s.setReviewComment(rs.getString("review_comment"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            s.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            s.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return s;
    }

    /**
     * Insert a new seller application.
     */
    public void save(SellerModel seller) {
        String sql = """
            INSERT INTO sellers (
                user_id,
                email,
                shop_name,
                shop_description,
                shop_logo_url,
                business_registration_number,
                business_address,
                contact_number,
                status,
                applied_at,
                created_at,
                updated_at
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime appliedAt = seller.getAppliedAt() != null ? seller.getAppliedAt() : now;

        jdbcTemplate.update(sql,
                seller.getUserId(),
                seller.getEmail(),                  // new: save seller email
                seller.getShopName(),
                seller.getShopDescription(),
                seller.getShopLogoUrl(),
                seller.getBusinessRegistrationNumber(),
                seller.getBusinessAddress(),
                seller.getContactNumber(),
                seller.getStatus() != null ? seller.getStatus().name() : null,
                appliedAt,
                now,
                now
        );
    }

    /**
     * Update seller basic profile fields (shop and business info).
     */
    public void updateProfile(SellerModel seller) {
        String sql = """
            UPDATE sellers SET
                shop_name = ?,
                shop_description = ?,
                shop_logo_url = ?,
                business_registration_number = ?,
                business_address = ?,
                contact_number = ?,
                updated_at = NOW()
            WHERE user_id = ?
            """;

        jdbcTemplate.update(sql,
                seller.getShopName(),
                seller.getShopDescription(),
                seller.getShopLogoUrl(),
                seller.getBusinessRegistrationNumber(),
                seller.getBusinessAddress(),
                seller.getContactNumber(),
                seller.getUserId()
        );
    }

    /**
     * Find seller by user ID.
     * Returns null if no seller record exists.
     */
    public SellerModel findByUserId(Long userId) {
        String sql = "SELECT * FROM sellers WHERE user_id = ?";

        List<SellerModel> list = jdbcTemplate.query(sql, this::mapRowToSeller, userId);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * New helper: find seller by email.
     * Returns null if no seller record exists for this email.
     */
    public SellerModel findByEmail(String email) {
        String sql = "SELECT * FROM sellers WHERE email = ?";

        List<SellerModel> list = jdbcTemplate.query(sql, this::mapRowToSeller, email);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * Check if a seller record exists for the given user.
     */
    public boolean existsByUserId(Long userId) {
        String sql = "SELECT COUNT(*) FROM sellers WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }

    /**
     * Find all sellers with the given status.
     * Useful for admin to list PENDING / APPROVED / REJECTED.
     */
    public List<SellerModel> findByStatus(SellerStatus status) {
        String sql = "SELECT * FROM sellers WHERE status = ?";
        return jdbcTemplate.query(sql, this::mapRowToSeller, status.name());
    }

    /**
     * Get all sellers.
     */
    public List<SellerModel> findAll() {
        String sql = "SELECT * FROM sellers";
        return jdbcTemplate.query(sql, this::mapRowToSeller);
    }

    /**
     * Update seller application status.
     * Admin sets new status, admin email and comment.
     */
    public void updateStatus(Long userId, SellerStatus newStatus, String adminEmail, String comment) {
        String sql = """
            UPDATE sellers SET
                status = ?,
                reviewed_by_admin = ?,
                review_comment = ?,
                reviewed_at = NOW(),
                updated_at = NOW()
            WHERE user_id = ?
            """;

        jdbcTemplate.update(sql,
                newStatus != null ? newStatus.name() : null,
                adminEmail,
                comment,
                userId
        );
    }

    /**
     * Count how many applications are still PENDING.
     */
    public int countPending() {
        String sql = "SELECT COUNT(*) FROM sellers WHERE status = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, SellerStatus.PENDING.name());
        return count == null ? 0 : count;
    }

    /**
     * Delete seller record by user_id.
     */
    public void deleteByUserId(Long userId) {
        String sql = "DELETE FROM sellers WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }
}
