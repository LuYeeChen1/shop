package com.backend.shop.Repository;

import com.backend.shop.Model.Admin.AdminModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class AdminRepository {

    private final JdbcTemplate jdbcTemplate;

    public AdminRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Row mapper for AdminModel.
     * Converts a database row into an AdminModel object.
     */
    private AdminModel mapRowToAdmin(ResultSet rs, int rowNum) throws SQLException {
        AdminModel admin = new AdminModel();
        admin.setUserId(rs.getLong("user_id"));
        admin.setFullName(rs.getString("full_name"));
        admin.setContactEmail(rs.getString("contact_email"));
        admin.setContactNumber(rs.getString("contact_number"));
        admin.setCanApproveSeller(rs.getBoolean("can_approve_seller"));
        admin.setCanManageProducts(rs.getBoolean("can_manage_products"));
        admin.setCanViewReports(rs.getBoolean("can_view_reports"));
        admin.setCanManageAgents(rs.getBoolean("can_manage_agents"));

        // Map timestamps if present
        Object createdAtObj = rs.getObject("created_at");
        if (createdAtObj != null) {
            admin.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }
        Object updatedAtObj = rs.getObject("updated_at");
        if (updatedAtObj != null) {
            admin.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        }

        return admin;
    }

    /**
     * Insert a new Admin record into admins table.
     */
    public void save(AdminModel admin) {
        String sql = "INSERT INTO admins (" +
                "user_id, full_name, contact_email, contact_number, " +
                "can_approve_seller, can_manage_products, can_view_reports, can_manage_agents" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                admin.getUserId(),
                admin.getFullName(),
                admin.getContactEmail(),
                admin.getContactNumber(),
                admin.isCanApproveSeller(),
                admin.isCanManageProducts(),
                admin.isCanViewReports(),
                admin.isCanManageAgents()
        );
    }

    /**
     * Update an existing Admin record.
     */
    public void update(AdminModel admin) {
        String sql = "UPDATE admins SET " +
                "full_name = ?, " +
                "contact_email = ?, " +
                "contact_number = ?, " +
                "can_approve_seller = ?, " +
                "can_manage_products = ?, " +
                "can_view_reports = ?, " +
                "can_manage_agents = ?, " +
                "updated_at = ? " +
                "WHERE user_id = ?";

        jdbcTemplate.update(sql,
                admin.getFullName(),
                admin.getContactEmail(),
                admin.getContactNumber(),
                admin.isCanApproveSeller(),
                admin.isCanManageProducts(),
                admin.isCanViewReports(),
                admin.isCanManageAgents(),
                LocalDateTime.now(),
                admin.getUserId()
        );
    }

    /**
     * Find an admin by user_id.
     */
    public AdminModel findByUserId(Long userId) {
        String sql = "SELECT * FROM admins WHERE user_id = ?";

        List<AdminModel> list = jdbcTemplate.query(sql, this::mapRowToAdmin, userId);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * Find an admin by contact_email.
     */
    public AdminModel findByEmail(String contactEmail) {
        String sql = "SELECT * FROM admins WHERE contact_email = ?";

        List<AdminModel> list = jdbcTemplate.query(sql, this::mapRowToAdmin, contactEmail);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * Check whether an admin record exists for the given user_id.
     */
    public boolean existsByUserId(Long userId) {
        String sql = "SELECT COUNT(*) FROM admins WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }

    /**
     * Check whether an admin record exists for the given email.
     */
    public boolean existsByEmail(String contactEmail) {
        String sql = "SELECT COUNT(*) FROM admins WHERE contact_email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, contactEmail);
        return count != null && count > 0;
    }

    /**
     * Get all admin records (for admin dashboard or debugging).
     */
    public List<AdminModel> findAll() {
        String sql = "SELECT * FROM admins";
        return jdbcTemplate.query(sql, this::mapRowToAdmin);
    }

    /**
     * Delete an admin record by user_id.
     */
    public void deleteByUserId(Long userId) {
        String sql = "DELETE FROM admins WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }

    /**
     * Delete an admin record by email.
     */
    public void deleteByEmail(String contactEmail) {
        String sql = "DELETE FROM admins WHERE contact_email = ?";
        jdbcTemplate.update(sql, contactEmail);
    }
}
