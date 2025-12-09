package com.trova_app.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserSignupDTO {

    @NotBlank(message = "Username is required")
    @Size(min = 7, message = "Username must be at least 7 characters long")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+]).{6,}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;  // Campo para la confirmación de la contraseña

    private String role;

    // Constructor vacío
    public UserSignupDTO() {}

    // Constructor con parámetros
    public UserSignupDTO(String username, String email, String name, String password, String confirmPassword, String role) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.password = password;
        this.confirmPassword = confirmPassword;
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

    public String getRoleOrDefault() {
        return (role != null && !role.isBlank()) ? role : "USER";
    }

    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }
}
