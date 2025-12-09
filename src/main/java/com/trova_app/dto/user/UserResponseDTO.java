package com.trova_app.dto.user;

import com.trova_app.enums.Role;
import com.trova_app.enums.Status;
import com.trova_app.model.User;

import java.util.UUID;

public class UserResponseDTO {

    private String username;
    private String name;
    private String email;
    private String token;
    private Status status;
    private Role role;
    private UUID id;

    public UserResponseDTO(User user) {
        this.username = user.getUsername();
        this.name = user.getName();
        this.status = user.getStatus();
        this.email = user.getEmail();
        this.id = user.getId();
        this.role = user.getRole();
        this.token = user.getCredential() != null ? user.getCredential().getToken() : null;
    }

    public static UserResponseDTO fromModel(User user) {
        return new UserResponseDTO(user);
    }

    //  Getters & Setters

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public Status getStatus() {
        return status;
    }

    public Role getRole() {
        return role;
    }

    public UUID getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
