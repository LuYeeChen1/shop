package com.backend.shop.Repository;

import com.backend.shop.Model.UserModel;
import com.backend.shop.Model.UserRole;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ============================================================
    // RowMapper for UserModel
    // ============================================================

    private UserModel mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        UserModel user = new UserModel();
        user.setId(rs.getLong("user_id"));
        user.setEmail(rs.getString("email"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));

        // role -> enum
        String roleStr = rs.getString("role");
        if (roleStr != null) {
            user.setRole(UserRole.valueOf(roleStr));
        }

        // Map created_at / updated_at if present
        if (rs.getTimestamp("created_at") != null) {
            user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }
        if (rs.getTimestamp("updated_at") != null) {
            user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        }

        return user;
    }

    // ============================================================
    // CREATE USER
    // ============================================================

    /**
     * Insert a new user into users table.
     * Returns the generated user_id (PK).
     */
    public Long createUser(String email, String password, String username, UserRole role) {

        String sql = """
            INSERT INTO users (email, username, password, role, created_at, updated_at)
            VALUES (?, ?, ?, ?, NOW(), NOW())
        """;

        jdbcTemplate.update(sql, email, username, password, role.name());

        // Retrieve generated user_id
        String fetchIdSql = "SELECT user_id FROM users WHERE email = ?";
        return jdbcTemplate.queryForObject(fetchIdSql, Long.class, email);
    }

    // ============================================================
    // FIND BY EMAIL
    // ============================================================

    public UserModel findByEmail(String email) {
        try {
            String sql = "SELECT * FROM users WHERE email = ?";
            return jdbcTemplate.queryForObject(sql, this::mapRowToUser, email);
        } catch (Exception e) {
            return null;
        }
    }

    // ============================================================
    // FIND PASSWORD ONLY
    // ============================================================

    public String findPasswordByEmail(String email) {
        try {
            String sql = "SELECT password FROM users WHERE email = ?";
            return jdbcTemplate.queryForObject(sql, String.class, email);
        } catch (Exception e) {
            return null;
        }
    }

    // ============================================================
    // FIND USERNAME ONLY
    // ============================================================

    public String findUsernameByEmail(String email) {
        try {
            String sql = "SELECT username FROM users WHERE email = ?";
            return jdbcTemplate.queryForObject(sql, String.class, email);
        } catch (Exception e) {
            return null;
        }
    }

    // ============================================================
    // FIND BY ID
    // ============================================================

    public UserModel findById(Long id) {
        try {
            String sql = "SELECT * FROM users WHERE user_id = ?";
            return jdbcTemplate.queryForObject(sql, this::mapRowToUser, id);
        } catch (Exception e) {
            return null;
        }
    }

    // ============================================================
    // FIND ALL
    // ============================================================

    public List<UserModel> findAll() {
        String sql = "SELECT * FROM users ORDER BY user_id ASC";
        return jdbcTemplate.query(sql, this::mapRowToUser);
    }

    // ============================================================
    // CHECK EMAIL EXISTS
    // ============================================================

    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    // ============================================================
    // UPDATE USER ROLE
    // ============================================================

    public void updateUserRole(Long userId, UserRole newRole) {
        String sql = """
            UPDATE users
            SET role = ?, updated_at = NOW()
            WHERE user_id = ?
        """;

        jdbcTemplate.update(sql, newRole.name(), userId);
    }
}
