package com.trovaApp.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class UserPatchDTO {

    private UUID userId;

    @Size(min = 7, message = "Username must be at least 7 characters long")
    private String newUsername;

    @Email(message = "Email is invalid")
    private String newEmail;

    private String newName;

    // Constructor vacío
    public UserPatchDTO() {}

    // Constructor con parámetros
    public UserPatchDTO(UUID userId, String newUsername, String newEmail, String newName) {
        this.userId = userId;
        this.newUsername = newUsername;
        this.newEmail = newEmail;
        this.newName = newName;
    }

    // Getters y setters
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
