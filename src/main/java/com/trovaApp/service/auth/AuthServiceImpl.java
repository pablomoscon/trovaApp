package com.trovaApp.service.auth;

import com.trovaApp.dto.user.UserSigninDTO;
import com.trovaApp.exception.TooManyLoginAttemptsException;
import com.trovaApp.model.User;
import com.trovaApp.security.UserPrincipal;
import com.trovaApp.security.bruteforce.LoginAttemptService;
import com.trovaApp.security.jwt.JwtProvider;
import com.trovaApp.utils.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private LoginAttemptService loginAttemptService;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtProvider jwtProvider, LoginAttemptService loginAttemptService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public User signInAndReturnJwt(UserSigninDTO signInRequest, HttpServletRequest request) {
        String username = signInRequest.getUsername();

        String ip = IpUtils.getClientIP(request);

        if (loginAttemptService.isBlocked(username, ip)) {
            throw new TooManyLoginAttemptsException("Too many failed login attempts. Try again later.");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, signInRequest.getPassword())
            );

            loginAttemptService.loginSucceeded(username, ip);

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            String jwt = jwtProvider.generateToken(userPrincipal);
            User signInUser = userPrincipal.getUser();

            if (signInUser.getCredential() != null) {
                signInUser.getCredential().setToken(jwt);
            } else {
                throw new RuntimeException("User's credential not found.");
            }

            return signInUser;

        } catch (Exception e) {
            loginAttemptService.loginFailed(username, ip);
            throw e;
        }
    }
}
