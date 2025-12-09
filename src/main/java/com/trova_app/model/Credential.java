package com.trova_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "credentials")
public class Credential {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Transient
    private String token;

    @OneToOne(mappedBy = "credential", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private User user;

    public Credential() {
    }

    public Credential(String password, User user) {
        this.password = password;
    }

    // Getters y Setters
    public UUID getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
