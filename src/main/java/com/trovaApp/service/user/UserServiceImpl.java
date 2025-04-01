package com.trovaApp.service.user;

import com.trovaApp.dto.SignupUserDto;
import com.trovaApp.enums.Role;
import com.trovaApp.model.Credential;
import com.trovaApp.model.User;
import com.trovaApp.repository.UserRepository;
import com.trovaApp.service.credential.CredentialService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CredentialService credentialService;


    @Override
    public User save(SignupUserDto signupUserDto) {
        // Check if the username is already in use
        Optional<User> existingUserByUsername = userRepository.findByUsername(signupUserDto.getUsername());
        if (existingUserByUsername.isPresent()) {
            // If username is already in use, throw an exception
            throw new IllegalArgumentException("Username is already in use");
        }

        // Check if the email is already in use
        Optional<User> existingUserByEmail = userRepository.findByEmail(signupUserDto.getEmail());
        if (existingUserByEmail.isPresent()) {
            // If email is already in use, throw an exception
            throw new IllegalArgumentException("Email is already in use");
        }

        // Create a new User instance
        User user = new User();
        user.setUsername(signupUserDto.getUsername());
        user.setEmail(signupUserDto.getEmail());
        user.setName(signupUserDto.getName());

        // Convert the role from String to Role enum with error handling
        Role role = getRoleFromString(signupUserDto.getRole());
        user.setRole(role);

        // Create and save credentials for the user
        Credential savedCredential = credentialService.createAndSaveCredential(signupUserDto.getPassword());
        user.setCredential(savedCredential);

        // Save the user with the assigned credentials
        return userRepository.save(user);
    }

    // Convert String role to Role enum, handling invalid values
    private Role getRoleFromString(String role) {
        try {
            return Role.valueOf(role);
        } catch (IllegalArgumentException e) {
            // If the value is invalid, handle the error appropriately
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    @Override
    public void changeRole(Role newRole, String username) {
        userRepository.updateUserRole(username, newRole);
    }
}
