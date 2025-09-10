package com.trovaDisc.trovaApp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConfig {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-in-ms}")
    private long expirationInMs;

    public String getJwtSecret() {
        return jwtSecret;
    }

    public long getExpirationInMs() {
        return expirationInMs;
    }
}
