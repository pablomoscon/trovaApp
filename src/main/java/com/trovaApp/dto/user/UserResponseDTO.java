package com.trovaApp.dto.user;

import com.trovaApp.model.User;

public class UserResponseDTO {

    private String username;
    private String email;
    private String token;

    public UserResponseDTO(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.token = user.getCredential() != null ? user.getCredential().getToken() : null;
    }

    // Getters para permitir la serialización JSON
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
