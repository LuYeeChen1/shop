package com.backend.shop.Repository;

import com.backend.shop.Model.SellerModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SellerRepository {

    private final JdbcTemplate jdbcTemplate;

    public SellerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Insert new seller
    public void save(SellerModel seller) {
        String sql = "INSERT INTO seller_table (email, username, password) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                seller.getEmail(),
                seller.getUsername(),
                seller.getPassword()
        );
    }

    // Find seller by email
    public SellerModel findByEmail(String email) {
        String sql = "SELECT email, username, password FROM seller_table WHERE email = ?";

        List<SellerModel> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
            SellerModel s = new SellerModel();
            s.setEmail(rs.getString("email"));
            s.setUsername(rs.getString("username"));
            s.setPassword(rs.getString("password"));
            return s;
        }, email);

        return list.isEmpty() ? null : list.get(0);
    }

    // Check if email exists
    public boolean exists(String email) {
        String sql = "SELECT COUNT(*) FROM seller_table WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    // Get all sellers
    public List<SellerModel> findAll() {
        String sql = "SELECT email, username, password FROM seller_table";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            SellerModel s = new SellerModel();
            s.setEmail(rs.getString("email"));
            s.setUsername(rs.getString("username"));
            s.setPassword(rs.getString("password"));
            return s;
        });
    }
}
