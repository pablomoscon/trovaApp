package com.trovaApp.service.auth;

import com.trovaApp.dto.SigninUserDto;
import com.trovaApp.model.User;
import com.trovaApp.security.UserPrincipal;
import com.trovaApp.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public User signInAndReturnJwt(SigninUserDto signInRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getUsername(),
                        signInRequest.getPassword()));

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String jwt = jwtProvider.generateToken(userPrincipal);

        User signInUser = userPrincipal.getUser();

        if (signInUser.getCredential() != null) {
            signInUser.getCredential().setToken(jwt);
        } else {
            throw new RuntimeException("User's credential not found.");
        }

        return signInUser;
    }
}
