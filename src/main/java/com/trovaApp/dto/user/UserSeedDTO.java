package com.trovaApp.dto.user;

public class UserSeedDTO {

    private String username;
    private String email;
    private String name;
    private String password;
    private String confirmPassword; // Optional
    private String role;

    // Constructors

    public UserSeedDTO() {
    }

    public UserSeedDTO(String username,
                       String email,
                       String name,
                       String password,
                       String confirmPassword,
                       String role) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.role = role;
    }

    // Getters & setters

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

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    // --- Utility methods (optional) ----------------------------------------

    @Override
    public String toString() {
        return "UserSeedDTO{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}