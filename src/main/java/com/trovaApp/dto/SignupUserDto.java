package com.trovaApp.dto;

public class SignupUserDto {

    private String username;
    private String email;
    private String name;
    private String password;
    private String role;

    // Constructor vacío
    public SignupUserDto() {}

    // Constructor con parámetros
    public SignupUserDto(String username, String email, String name, String password, String role) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    // Getters y Setters
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
