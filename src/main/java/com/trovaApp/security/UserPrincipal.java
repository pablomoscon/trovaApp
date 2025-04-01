package com.trovaApp.security;

import com.trovaApp.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public class UserPrincipal implements UserDetails {

    private String id;
    private String username;
    transient private String password;
    transient private User user;
    private Set<GrantedAuthority> authorities;

    // Private constructor to enforce instantiation through the Builder
    private UserPrincipal(String id, String username, User user, String password, Set<GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.user = user;
        this.password = password;
        this.authorities = authorities;
    }

    // Getter for ID
    public String getId() {
        return id;
    }

    // Getter for User
    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // --- BUILDER ---
    public static class Builder {
        private String id;
        private String username;
        private User user;
        private String password;
        private Set<GrantedAuthority> authorities;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder authorities(Set<GrantedAuthority> authorities) {
            this.authorities = authorities;
            return this;
        }

        public UserPrincipal build() {
            return new UserPrincipal(id, username, user, password, authorities);
        }
    }

    // Static method to initialize the builder
    public static Builder builder() {
        return new Builder();
    }
}
