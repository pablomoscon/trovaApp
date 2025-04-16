package com.trovaApp.dto.user;

public class UserSigninDTO {

    private String username;
    private String password;


    // Constructor vacío
    public UserSigninDTO() {}

    // Constructor con parámetros
    public UserSigninDTO(String username, String password) {
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
