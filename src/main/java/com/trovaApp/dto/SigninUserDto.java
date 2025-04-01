package com.trovaApp.dto;

public class SigninUserDto {

    private String username;
    private String password;


    // Constructor vacío
    public SigninUserDto() {}

    // Constructor con parámetros
    public SigninUserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
