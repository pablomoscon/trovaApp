package com.trova_app.service.auth;

import com.trova_app.dto.user.UserSigninDTO;
import com.trova_app.exception.TooManyLoginAttemptsException;
import com.trova_app.model.User;
import com.trova_app.security.UserPrincipal;
import com.trova_app.security.bruteforce.LoginAttemptService;
import com.trova_app.security.jwt.JwtProvider;
import com.trova_app.service.user.UserService;
import com.trova_app.util.IpUtils;
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
    private final LoginAttemptService loginAttemptService;
    private final UserService userService;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtProvider jwtProvider,
                           LoginAttemptService loginAttemptService,
                           UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.loginAttemptService = loginAttemptService;
        this.userService = userService;
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

            userService.updateLastLogin(signInUser.getId());

            return signInUser;

        } catch (Exception exception) {
            loginAttemptService.loginFailed(username, ip);
            userService.incrementFailedLoginAttempts(username);

            throw exception;
        }
    }
}
