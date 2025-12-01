package com.backend.shop.Repository;

import com.backend.shop.Model.Seller.SellerModel;
import com.backend.shop.Model.Seller.SellerStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SellerRepository {

    private final JdbcTemplate jdbcTemplate;

    public SellerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private SellerModel mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        SellerModel s = new SellerModel();

        s.setUserId(rs.getLong("user_id"));
        s.setShopName(rs.getString("shop_name"));
        s.setShopDescription(rs.getString("shop_description"));
        s.setShopLogoUrl(rs.getString("shop_logo_url"));

        s.setBusinessRegistrationNumber(rs.getString("business_reg_no"));
        s.setBusinessAddress(rs.getString("business_address"));
        s.setContactNumber(rs.getString("contact_number"));

        // Convert VARCHAR → Enum
        String statusStr = rs.getString("status");
        if (statusStr != null) {
            s.setStatus(SellerStatus.valueOf(statusStr));
        }

        // timestamps
        java.sql.Timestamp appliedAt = rs.getTimestamp("applied_at");
        if (appliedAt != null) s.setAppliedAt(appliedAt.toLocalDateTime());

        java.sql.Timestamp reviewedAt = rs.getTimestamp("reviewed_at");
        if (reviewedAt != null) s.setReviewedAt(reviewedAt.toLocalDateTime());

        s.setReviewedByAdmin(rs.getString("reviewed_by_admin"));
        s.setReviewComment(rs.getString("review_comment"));

        return s;
    }

    // Save base seller info
    public void save(SellerModel seller) {
        String sql = """
            INSERT INTO seller_table 
            (user_id, shop_name, shop_description, shop_logo_url, business_reg_no, 
             business_address, contact_number, status, applied_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        jdbcTemplate.update(sql,
                seller.getUserId(),
                seller.getShopName(),
                seller.getShopDescription(),
                seller.getShopLogoUrl(),
                seller.getBusinessRegistrationNumber(),
                seller.getBusinessAddress(),
                seller.getContactNumber(),
                seller.getStatus() != null ? seller.getStatus().name() : null,
                seller.getAppliedAt()
        );
    }

    // Find by user ID
    public SellerModel findByUserId(Long userId) {
        String sql = "SELECT * FROM seller_table WHERE user_id = ?";
        List<SellerModel> list = jdbcTemplate.query(sql, this::mapRow, userId);
        return list.isEmpty() ? null : list.get(0);
    }

    // Admin: find all seller applications by enum status
    public List<SellerModel> findByStatus(SellerStatus status) {
        String sql = "SELECT * FROM seller_table WHERE status = ?";
        return jdbcTemplate.query(sql, this::mapRow, status.name());
    }

    // Admin: update seller application status
    public void updateStatus(Long userId, SellerStatus newStatus, String adminEmail, String comment) {
        String sql = """
            UPDATE seller_table SET 
            status = ?, 
            reviewed_by_admin = ?, 
            review_comment = ?, 
            reviewed_at = NOW()
            WHERE user_id = ?
        """;
        jdbcTemplate.update(sql, newStatus.name(), adminEmail, comment, userId);
    }

    // Count pending applications
    public int countPending() {
        String sql = "SELECT COUNT(*) FROM seller_table WHERE status = 'PENDING'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count == null ? 0 : count;
    }
}
