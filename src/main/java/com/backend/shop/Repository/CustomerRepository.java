package com.backend.shop.Repository;

import com.backend.shop.Model.CustomerModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerRepository {

    private final JdbcTemplate jdbcTemplate;

    public CustomerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Insert new customer
    public void save(CustomerModel customer) {
        String sql = "INSERT INTO customer_table (email, username, password) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                customer.getEmail(),
                customer.getUsername(),
                customer.getPassword()
        );
    }

    // Find customer by email
    public CustomerModel findByEmail(String email) {
        String sql = "SELECT email, username, password FROM customer_table WHERE email = ?";

        List<CustomerModel> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
            CustomerModel c = new CustomerModel();
            c.setEmail(rs.getString("email"));
            c.setUsername(rs.getString("username"));
            c.setPassword(rs.getString("password"));
            return c;
        }, email);

        return list.isEmpty() ? null : list.get(0);
    }

    // Check if email exists
    public boolean exists(String email) {
        String sql = "SELECT COUNT(*) FROM customer_table WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    // Get all customers
    public List<CustomerModel> findAll() {
        String sql = "SELECT email, username, password FROM customer_table";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            CustomerModel c = new CustomerModel();
            c.setEmail(rs.getString("email"));
            c.setUsername(rs.getString("username"));
            c.setPassword(rs.getString("password"));
            return c;
        });
    }
}
