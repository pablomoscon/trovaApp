package com.trovaApp.service.user;

import com.trovaApp.dto.user.UserPatchDTO;
import com.trovaApp.dto.user.UserSignupDTO;
import com.trovaApp.enums.Role;
import com.trovaApp.exception.*;
import com.trovaApp.model.Credential;
import com.trovaApp.model.User;
import com.trovaApp.repository.UserRepository;
import com.trovaApp.service.credential.CredentialService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CredentialService credentialService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CredentialService credentialService) {
        this.userRepository = userRepository;
        this.credentialService = credentialService;
    }

    @Override
    public User save(UserSignupDTO signupUserDto) {
        // Check if the username is already in use
        Optional<User> existingUserByUsername = userRepository.findByUsername(signupUserDto.getUsername());
        if (existingUserByUsername.isPresent()) {
            throw new UsernameAlreadyExistsException("Username is already in use");
        }

        // Check if the email is already in use
        Optional<User> existingUserByEmail = userRepository.findByEmail(signupUserDto.getEmail());
        if (existingUserByEmail.isPresent()) {
            throw new EmailAlreadyExistsException("Email is already in use");
        }

        // Validate password format
        String password = signupUserDto.getPassword();
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+]).{6,}$")) {
            throw new InvalidPasswordFormatException("Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character");
        }

        // Validate the username length
        if (signupUserDto.getUsername().length() < 7) {
            throw new IllegalArgumentException("Username must be at least 7 characters long");
        }

        // Check if the passwords match
        if (!signupUserDto.isPasswordMatching()) {
            throw new PasswordsDoNotMatchException("Passwords do not match");
        }

        // Create a new User instance
        User user = new User();
        user.setUsername(signupUserDto.getUsername());
        user.setEmail(signupUserDto.getEmail());
        user.setName(signupUserDto.getName());

        // Convert the role from String to Role enum
        Role role = getRoleFromString(signupUserDto.getRoleOrDefault());
        user.setRole(role);

        // Create and save credentials for the user
        Credential savedCredential = credentialService.createAndSaveCredential(signupUserDto.getPassword());
        user.setCredential(savedCredential);

        // Save the user
        return userRepository.save(user);
    }

    private Role getRoleFromString(String role) {
        try {
            return Role.valueOf(role);
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException("Invalid role: " + role);
        }
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional <User> findById(UUID userId) {
        return userRepository.findById(userId);
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

    @Transactional
    @Override
    public User patchUser(UUID userId, UserPatchDTO userPatchDTO) {
        User user = this.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

        String newUsername = userPatchDTO.getNewUsername();
        String newEmail = userPatchDTO.getNewEmail();
        String newName = userPatchDTO.getNewName();

        if (newUsername != null && !newUsername.equals(user.getUsername())) {
            if (newUsername.length() < 7) {
                throw new IllegalArgumentException("Username must be at least 7 characters long");
            }
            if (userRepository.findByUsername(newUsername).isPresent()) {
                throw new UsernameAlreadyExistsException("Username is already in use");
            }
            user.setUsername(newUsername);
        }

        if (newEmail != null && !newEmail.equals(user.getEmail())) {
            if (userRepository.findByEmail(newEmail).isPresent()) {
                throw new EmailAlreadyExistsException("Email is already in use");
            }
            user.setEmail(newEmail);
        }

        if (newName != null && !newName.equals(user.getName())) {
            user.setName(newName);
        }

        return userRepository.save(user);
    }

}
