package com.backend.shop.Repository;

import com.backend.shop.Model.Customer.CustomerModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CustomerRepository {

    private final JdbcTemplate jdbcTemplate;

    public CustomerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Row mapper for CustomerModel.
     * Converts a database row into a CustomerModel object.
     */
    private CustomerModel mapRowToCustomer(ResultSet rs, int rowNum) throws SQLException {
        CustomerModel customer = new CustomerModel();
        customer.setUserId(rs.getLong("user_id"));
        customer.setFullName(rs.getString("full_name"));
        customer.setPhoneNumber(rs.getString("phone_number"));
        customer.setDefaultShippingAddress(rs.getString("default_shipping_address"));
        customer.setBillingAddress(rs.getString("billing_address"));
        customer.setPreferredLanguage(rs.getString("preferred_language"));
        customer.setPreferredCurrency(rs.getString("preferred_currency"));
        customer.setLoyaltyPoints(rs.getInt("loyalty_points"));
        return customer;
    }

    /**
     * Insert a new customer.
     */
    public void save(CustomerModel customer) {
        String sql = "INSERT INTO customer_table (" +
                "user_id, full_name, phone_number, default_shipping_address, " +
                "billing_address, preferred_language, preferred_currency, loyalty_points" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                customer.getUserId(),
                customer.getFullName(),
                customer.getPhoneNumber(),
                customer.getDefaultShippingAddress(),
                customer.getBillingAddress(),
                customer.getPreferredLanguage(),
                customer.getPreferredCurrency(),
                customer.getLoyaltyPoints()
        );
    }

    /**
     * Update an existing customer by user_id.
     */
    public void update(CustomerModel customer) {
        String sql = "UPDATE customer_table SET " +
                "full_name = ?, " +
                "phone_number = ?, " +
                "default_shipping_address = ?, " +
                "billing_address = ?, " +
                "preferred_language = ?, " +
                "preferred_currency = ?, " +
                "loyalty_points = ? " +
                "WHERE user_id = ?";

        jdbcTemplate.update(sql,
                customer.getFullName(),
                customer.getPhoneNumber(),
                customer.getDefaultShippingAddress(),
                customer.getBillingAddress(),
                customer.getPreferredLanguage(),
                customer.getPreferredCurrency(),
                customer.getLoyaltyPoints(),
                customer.getUserId()
        );
    }

    /**
     * Find customer by user_id.
     */
    public CustomerModel findByUserId(Long userId) {
        String sql = "SELECT * FROM customer_table WHERE user_id = ?";

        List<CustomerModel> list = jdbcTemplate.query(sql, this::mapRowToCustomer, userId);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * Find customer by phone number.
     */
    public CustomerModel findByPhoneNumber(String phoneNumber) {
        String sql = "SELECT * FROM customer_table WHERE phone_number = ?";

        List<CustomerModel> list = jdbcTemplate.query(sql, this::mapRowToCustomer, phoneNumber);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * Check whether customer exists by user_id.
     */
    public boolean existsByUserId(Long userId) {
        String sql = "SELECT COUNT(*) FROM customer_table WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }

    /**
     * Get all customers.
     */
    public List<CustomerModel> findAll() {
        String sql = "SELECT * FROM customer_table";
        return jdbcTemplate.query(sql, this::mapRowToCustomer);
    }

    /**
     * Delete customer by user_id.
     */
    public void deleteByUserId(Long userId) {
        String sql = "DELETE FROM customer_table WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }
}
