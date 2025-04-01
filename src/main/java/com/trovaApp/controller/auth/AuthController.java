package com.trovaApp.controller.auth;

import com.trovaApp.dto.ResponseUserDto;
import com.trovaApp.dto.SigninUserDto;
import com.trovaApp.dto.SignupUserDto;
import com.trovaApp.model.User;
import com.trovaApp.service.auth.AuthService;
import com.trovaApp.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("sign-up")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignupUserDto signupUserDto) {
        try {
            // Attempt to create the user and save credentials
            User savedUser = userService.save(signupUserDto);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED); // User successfully created
        } catch (IllegalArgumentException e) {
            // If email or username is already in use, return a conflict response
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("sign-in")
    public ResponseEntity<?> signIn(@RequestBody SigninUserDto signinUserDto) {
        User user = authService.signInAndReturnJwt(signinUserDto);
        return ResponseEntity.ok(new ResponseUserDto(user));
    }
}