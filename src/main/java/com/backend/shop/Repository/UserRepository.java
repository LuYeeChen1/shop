package com.backend.shop.Repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 1. Check if email exists across all role tables
    public boolean emailExists(String email) {
        return existsInAdmin(email) || existsInCustomer(email) || existsInSeller(email);
    }

    public boolean existsInAdmin(String email) {
        String sql = "SELECT COUNT(*) FROM admin_table WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    public boolean existsInCustomer(String email) {
        String sql = "SELECT COUNT(*) FROM customer_table WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    public boolean existsInSeller(String email) {
        String sql = "SELECT COUNT(*) FROM seller_table WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    // 2. Detect which role this email belongs to
    public String detectRole(String email) {

        if (existsInAdmin(email)) {
            return "ADMIN";
        }
        if (existsInCustomer(email)) {
            return "CUSTOMER";
        }
        if (existsInSeller(email)) {
            return "SELLER";
        }

        return null; // Not found in any table
    }
}
