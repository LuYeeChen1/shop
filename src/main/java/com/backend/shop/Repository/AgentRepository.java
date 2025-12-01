package com.backend.shop.Repository;

import com.backend.shop.Model.Agent.AgentModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Repository
public class AgentRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AgentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Convert specialties list to JSON string.
     */
    private String specialtiesToJson(List<String> list) {
        try {
            return list != null ? objectMapper.writeValueAsString(list) : "[]";
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert specialties to JSON", e);
        }
    }

    /**
     * Convert JSON string back to specialties list.
     */
    private List<String> jsonToSpecialties(String json) {
        try {
            return json != null
                    ? objectMapper.readValue(json, List.class)
                    : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * Row mapper for AgentModel.
     */
    private AgentModel mapRowToAgent(ResultSet rs, int rowNum) throws SQLException {
        AgentModel agent = new AgentModel();
        agent.setUserId(rs.getLong("user_id"));
        agent.setFullName(rs.getString("full_name"));
        agent.setContactEmail(rs.getString("contact_email"));
        agent.setContactNumber(rs.getString("contact_number"));
        agent.setEmployeeId(rs.getString("employee_id"));
        agent.setDepartment(rs.getString("department"));
        agent.setShift(rs.getString("shift"));
        agent.setOnline(rs.getBoolean("online"));
        agent.setActiveTickets(rs.getInt("active_tickets"));

        agent.setSpecialties(jsonToSpecialties(rs.getString("specialties")));

        agent.setAvailable(rs.getBoolean("available"));
        return agent;
    }

    /**
     * Insert new agent.
     */
    public void save(AgentModel agent) {
        String sql = "INSERT INTO agent_table (" +
                "user_id, full_name, contact_email, contact_number, " +
                "employee_id, department, shift, online, active_tickets, specialties, available" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                agent.getUserId(),
                agent.getFullName(),
                agent.getContactEmail(),
                agent.getContactNumber(),
                agent.getEmployeeId(),
                agent.getDepartment(),
                agent.getShift(),
                agent.isOnline(),
                agent.getActiveTickets(),
                specialtiesToJson(agent.getSpecialties()),
                agent.isAvailable()
        );
    }

    /**
     * Update agent.
     */
    public void update(AgentModel agent) {
        String sql = "UPDATE agent_table SET " +
                "full_name = ?, " +
                "contact_email = ?, " +
                "contact_number = ?, " +
                "employee_id = ?, " +
                "department = ?, " +
                "shift = ?, " +
                "online = ?, " +
                "active_tickets = ?, " +
                "specialties = ?, " +
                "available = ? " +
                "WHERE user_id = ?";

        jdbcTemplate.update(sql,
                agent.getFullName(),
                agent.getContactEmail(),
                agent.getContactNumber(),
                agent.getEmployeeId(),
                agent.getDepartment(),
                agent.getShift(),
                agent.isOnline(),
                agent.getActiveTickets(),
                specialtiesToJson(agent.getSpecialties()),
                agent.isAvailable(),
                agent.getUserId()
        );
    }

    /**
     * Find agent by user ID.
     */
    public AgentModel findByUserId(Long userId) {
        String sql = "SELECT * FROM agent_table WHERE user_id = ?";

        List<AgentModel> list = jdbcTemplate.query(sql, this::mapRowToAgent, userId);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * Check if agent exists.
     */
    public boolean existsByUserId(Long userId) {
        String sql = "SELECT COUNT(*) FROM agent_table WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }

    /**
     * Get all agents.
     */
    public List<AgentModel> findAll() {
        String sql = "SELECT * FROM agent_table";
        return jdbcTemplate.query(sql, this::mapRowToAgent);
    }

    /**
     * Find all available agents.
     */
    public List<AgentModel> findAvailableAgents() {
        String sql = "SELECT * FROM agent_table WHERE available = TRUE";
        return jdbcTemplate.query(sql, this::mapRowToAgent);
    }

    /**
     * Delete agent by user_id.
     */
    public void deleteByUserId(Long userId) {
        String sql = "DELETE FROM agent_table WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }
}
