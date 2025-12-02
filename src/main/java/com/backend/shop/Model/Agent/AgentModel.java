package com.backend.shop.Model.Agent;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * AgentModel represents support agent information and status.
 * It is mapped to the "agents" table in the database.
 */
@Entity
@Table(name = "agents")
public class AgentModel {

    // Primary key, also foreign key to users.id
    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "full_name", length = 200)
    private String fullName;

    @Column(name = "contact_email", length = 200)
    private String contactEmail;

    @Column(name = "contact_number", length = 50)
    private String contactNumber;

    @Column(name = "employee_id", length = 100)
    private String employeeId;

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "shift", length = 100)
    private String shift;

    @Column(name = "online")
    private boolean online;

    @Column(name = "active_tickets")
    private int activeTickets;

    /**
     * List of specialties for the agent.
     * Stored as a JSON string in the "specialties" column by the repository.
     * JPA mapping is not directly used for this field.
     */
    @Transient
    private List<String> specialties;

    @Column(name = "available")
    private boolean available;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Default constructor required by JPA
    public AgentModel() {
    }

    // Getters and setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public int getActiveTickets() {
        return activeTickets;
    }

    public void setActiveTickets(int activeTickets) {
        this.activeTickets = activeTickets;
    }

    public List<String> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<String> specialties) {
        this.specialties = specialties;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
