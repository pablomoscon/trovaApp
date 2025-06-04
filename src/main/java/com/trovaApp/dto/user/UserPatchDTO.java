package com.trovaApp.dto.user;

import com.trovaApp.enums.Role;
import com.trovaApp.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class UserPatchDTO {

    private UUID userId;

    @Size(min = 7, message = "Username must be at least 7 characters long")
    private String username;

    @Email(message = "Email is invalid")
    private String email;

    private String name;
    private Role role;
    private Status status;


    // Empty constructor
    public UserPatchDTO() {}

    // Constructor with all fields
    public UserPatchDTO(UUID userId,
                        String username,
                        String email,
                        String name,
                        Role role,
                        Status status) {

        this.userId = userId;
        this.username= username;
        this.email = email;
        this.name = name;
        this.role = role;
        this.status = status;
    }

    // Getters and setters
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String newUsername) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setNewEmail(String email) {
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
}
