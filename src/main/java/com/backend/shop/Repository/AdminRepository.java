package com.backend.shop.Repository;

import com.backend.shop.Model.AdminModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdminRepository {

    private final JdbcTemplate jdbcTemplate;

    public AdminRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Insert new admin
    public void save(AdminModel admin) {
        String sql = "INSERT INTO admin_table (email, username, password) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                admin.getEmail(),
                admin.getUsername(),
                admin.getPassword()
        );
    }

    // Find admin by email (PK)
    public AdminModel findByEmail(String email) {
        String sql = "SELECT email, username, password FROM admin_table WHERE email = ?";

        List<AdminModel> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
            AdminModel a = new AdminModel();
            a.setEmail(rs.getString("email"));
            a.setUsername(rs.getString("username"));
            a.setPassword(rs.getString("password"));
            return a;
        }, email);

        return list.isEmpty() ? null : list.get(0);
    }

    // Check if email exists
    public boolean exists(String email) {
        String sql = "SELECT COUNT(*) FROM admin_table WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    // Get all admins (for管理页面 / 调试)
    public List<AdminModel> findAll() {
        String sql = "SELECT email, username, password FROM admin_table";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            AdminModel a = new AdminModel();
            a.setEmail(rs.getString("email"));
            a.setUsername(rs.getString("username"));
            a.setPassword(rs.getString("password"));
            return a;
        });
    }
}
