package com.trova_app.dto.user;

import com.trova_app.enums.Role;
import com.trova_app.enums.Status;
import com.trova_app.model.Activity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserFindAllResponseDTO {

    private UUID id;
    private String username;
    private String email;
    private String name;
    private Role role;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private int failedLoginAttempts;
    private List<String> activities;

    public UserFindAllResponseDTO() {}

    public UserFindAllResponseDTO(com.trova_app.model.User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
        this.role = user.getRole();
        this.status = user.getStatus();
        this.createdAt = user.getCreatedAt();
        this.lastLogin = user.getLastLogin();
        this.failedLoginAttempts = user.getFailedLoginAttempts();
        this.activities = loadActivities(user);
    }

    // Método para cargar las actividades de manera controlada
    private List<String> loadActivities(com.trova_app.model.User user) {
        return user.getActivities().stream()
                .map(Activity::getDescription)
                .collect(Collectors.toList());
    }

    // Getters and setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public List<String> getActivities() {
        return activities;
    }

    public void setActivities(List<String> activities) {
        this.activities = activities;
    }
}
