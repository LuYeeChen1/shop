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

    private AdminModel mapRowToAdmin(ResultSet rs, int rowNum) throws SQLException {
        AdminModel admin = new AdminModel();
        admin.setAdminId(rs.getLong("admin_id"));
        admin.setUsername(rs.getString("username"));
        admin.setPassword(rs.getString("password"));
        admin.setFullName(rs.getString("full_name"));
        admin.setContactEmail(rs.getString("contact_email"));
        admin.setContactNumber(rs.getString("contact_number"));
        admin.setCanApproveSeller(rs.getBoolean("can_approve_seller"));
        admin.setCanManageProducts(rs.getBoolean("can_manage_products"));
        admin.setCanViewReports(rs.getBoolean("can_view_reports"));
        admin.setCanManageAgents(rs.getBoolean("can_manage_agents"));

        if (rs.getTimestamp("created_at") != null) {
            admin.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }
        if (rs.getTimestamp("updated_at") != null) {
            admin.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        }

        return admin;
    }

    public AdminModel findByUsername(String username) {
        String sql = "SELECT * FROM admins WHERE username = ?";
        List<AdminModel> list = jdbcTemplate.query(sql, this::mapRowToAdmin, username);
        return list.isEmpty() ? null : list.get(0);
    }

    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM admins WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    public void save(AdminModel admin) {
        String sql = "INSERT INTO admins (" +
                "username, password, full_name, contact_email, contact_number, " +
                "can_approve_seller, can_manage_products, can_view_reports, can_manage_agents, created_at" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                admin.getUsername(),
                admin.getPassword(),
                admin.getFullName(),
                admin.getContactEmail(),
                admin.getContactNumber(),
                admin.isCanApproveSeller(),
                admin.isCanManageProducts(),
                admin.isCanViewReports(),
                admin.isCanManageAgents(),
                LocalDateTime.now()
        );
    }
}
