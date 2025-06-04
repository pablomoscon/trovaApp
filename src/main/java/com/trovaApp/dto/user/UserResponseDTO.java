package com.trovaApp.dto.user;

import com.trovaApp.model.User;

import java.util.UUID;

public class UserResponseDTO {

    private String username;
    private String email;
    private String token;
    private UUID id;

    public UserResponseDTO(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.id = user.getId();

        this.token = user.getCredential() != null ? user.getCredential().getToken() : null;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public UUID getId() { return id;}
}
