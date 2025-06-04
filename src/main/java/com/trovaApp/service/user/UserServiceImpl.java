package com.trovaApp.service.user;

import com.trovaApp.dto.user.UserPatchDTO;
import com.trovaApp.dto.user.UserSignupDTO;
import com.trovaApp.enums.Role;
import com.trovaApp.enums.Status;
import com.trovaApp.exception.*;
import com.trovaApp.model.Credential;
import com.trovaApp.model.User;
import com.trovaApp.repository.UserRepository;
import com.trovaApp.service.activity.ActivityService;
import com.trovaApp.service.credential.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CredentialService credentialService;
    private final ActivityService activityService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           CredentialService credentialService,
                           ActivityService activityService) {
        this.userRepository = userRepository;
        this.credentialService = credentialService;
        this.activityService = activityService;
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
            throw new InvalidPasswordFormatException(
                    "Password must contain at least one uppercase letter, one lowercase letter, " +
                            "one digit, and one special character");
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

        activityService.logActivity(user, "Create user: " + user.getUsername());

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

    @Transactional
    @Override
    public List<User> findAll() {
        return userRepository.findAllWithActivities();
    }

    @Transactional(readOnly = true)
    @Override
    public User findByIdWithActivities(UUID userId) {
        return userRepository.findByIdWithActivities(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
    }

    @Transactional
    @Override
    public User findById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
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
        User user = this.findById(userId);

        String username = userPatchDTO.getUsername();
        String email = userPatchDTO.getEmail();
        String name = userPatchDTO.getName();
        Role role = userPatchDTO.getRole();
        Status status = userPatchDTO.getStatus();

        if (username != null && !username.equals(user.getUsername())) {
            if (username.length() < 7) {
                throw new IllegalArgumentException("Username must be at least 7 characters long");
            }
            if (userRepository.findByUsername(username).isPresent()) {
                throw new UsernameAlreadyExistsException("Username is already in use");
            }
            user.setUsername(username);
        }

        if (email != null && !email.equals(user.getEmail())) {
            if (userRepository.findByEmail(email).isPresent()) {
                throw new EmailAlreadyExistsException("Email is already in use");
            }
            user.setEmail(email);
        }

        if (name != null && !name.equals(user.getName())) {
            user.setName(name);
        }

        if (role != null && !role.equals(user.getRole())) {
            user.setRole(role);
        }

        if (status != null && !status.equals(user.getStatus())) {
            user.setStatus(status);
        }

        activityService.logActivity(user, "Edit user: " + user.getUsername());
        return userRepository.save(user);
    }

    @Override
    public void delete(UUID userId) {
        User user = this.findById(userId);

        user.setStatus(Status.DELETED);
        userRepository.save(user);
    }

    @Override
    public void suspendUser(UUID userId) {
        User user = this.findById(userId);

        user.setStatus(Status.SUSPENDED);
        userRepository.save(user);
    }

    @Override
    public void updateLastLogin(UUID userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        } else {
            throw new RuntimeException("User with ID " + userId + " not found");
        }
    }

    @Transactional
    @Override
    public void incrementFailedLoginAttempts(String username) {
        User user = this.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));

        user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
        userRepository.save(user);
    }
}
