package com.trova_app.security.jwt;

import com.trova_app.security.UserPrincipal;
import com.trova_app.util.SecurityUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtProviderImpl implements JwtProvider {

    @Value("${app.jwt.secret}")
    private String JWT_SECRET;

    @Value("${app.jwt.expiration-in-ms}")
    private Long JWT_EXPIRATION_IN_MS;

    @Override
    public String generateToken(UserPrincipal auth) {
        // Convert the authorities from the GrantedAuthority objects to a comma-separated string
        String authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // Generate the SecretKey for signing the JWT
        SecretKey key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

        // Build the JWT token with the necessary claims
        return Jwts.builder()
                .subject(auth.getUsername())  // Set the username as the subject
                .claim("roles", authorities)  // Set the roles as a claim
                .claim("userId", auth.getId().toString())  // Convert UUID to String before adding as claim
                .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_IN_MS))  // Set expiration time
                .signWith(key)  // Sign the token with the secret key
                .compact();  // Build the JWT token
    }

    @Override
    public Authentication getAuthentication(HttpServletRequest request) {
        // Extract claims from the token
        Claims claims = extractClaims(request);
        if (claims == null) {
            return null;
        }

        // Get the username from the subject
        String username = claims.getSubject();
        // Convert the userId from String to UUID
        UUID userId = UUID.fromString(claims.get("userId", String.class));  // Convert String to UUID

        // Convert the roles from a comma-separated string to a set of GrantedAuthority objects
        Set<GrantedAuthority> authorities = Arrays.stream(claims.get("roles").toString().split(","))
                .map(SecurityUtils::convertToAuthority)
                .collect(Collectors.toSet());

        // Create the UserDetails object with the username, authorities, and userId
        UserDetails userDetails = UserPrincipal.builder()
                .username(username)
                .authorities(authorities)
                .id(userId)
                .build();

        // Return null if username is not found
        if (username == null) {
            return null;
        }

        // Create and return the Authentication object
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    @Override
    public boolean isTokenValid(HttpServletRequest request) {
        // Extract claims from the token
        Claims claims = extractClaims(request);

        // Return false if claims are null
        if (claims == null) {
            return false;
        }

        // Return false if the token has expired
        if (claims.getExpiration().before(new Date())) {
            return false;
        }

        return true;  // The token is valid
    }

    private Claims extractClaims(HttpServletRequest request) {
        // Extract the token from the request
        String token = SecurityUtils.extractAuthTokenFromRequest(request);
        if (token == null) {
            return null;  // Return null if no token is found
        }

        // Generate the SecretKey for verifying the JWT signature
        SecretKey key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
