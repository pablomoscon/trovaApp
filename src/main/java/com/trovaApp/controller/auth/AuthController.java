package com.trovaApp.controller.auth;

import com.trovaApp.dto.user.UserResponseDTO;
import com.trovaApp.dto.user.UserSigninDTO;
import com.trovaApp.dto.user.UserSignupDTO;
import com.trovaApp.model.User;
import com.trovaApp.service.auth.AuthService;
import com.trovaApp.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "auth", description = "Operations related to Authentication")
@RestController
@RequestMapping("auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @Autowired
    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @Operation(summary = "Register a new user")
    @PostMapping("sign-up")
    public ResponseEntity<Object> signUp(@RequestBody @Valid UserSignupDTO signupUserDto) {
        try {
            // Attempt to create the user and save credentials
            User savedUser = userService.save(signupUserDto);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED); // User successfully created
        } catch (IllegalArgumentException e) {
            // If email or username is already in use, return a conflict response
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @Operation(summary = "Authenticate user and return JWT token")
    @PostMapping("sign-in")
    public ResponseEntity<UserResponseDTO> signIn(@RequestBody UserSigninDTO signinUserDto, HttpServletRequest request) {
        User user = authService.signInAndReturnJwt(signinUserDto, request);
        return ResponseEntity.ok(new UserResponseDTO(user));
    }
}
