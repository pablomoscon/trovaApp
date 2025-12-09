package com.trova_app.service.user;

import com.trova_app.dto.user.UserPatchDTO;
import com.trova_app.dto.user.UserSignupDTO;
import com.trova_app.enums.Role;
import com.trova_app.enums.Status;
import com.trova_app.exception.*;
import com.trova_app.model.Credential;
import com.trova_app.model.User;
import com.trova_app.repository.UserRepository;
import com.trova_app.service.activity.ActivityService;
import com.trova_app.service.credential.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    private static final String NOT_FOUND = " not found";

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

        User savedUser = userRepository.save(user);

        activityService.logActivity(savedUser, "Create user: " + savedUser.getUsername());

        return savedUser;
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
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAllByStatusNot(Status.DELETED, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public User findByIdWithActivities(UUID userId) {
        return userRepository.findWithActivitiesById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + NOT_FOUND));
    }

    @Transactional
    @Override
    public User findById(UUID userId) {
        return doFindById(userId);
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
        User user = doFindById(userId);

        updateUsername(user, userPatchDTO.getUsername());
        updateEmail(user, userPatchDTO.getEmail());
        updateName(user, userPatchDTO.getName());
        updateRole(user, userPatchDTO.getRole());
        updateStatus(user, userPatchDTO.getStatus());

        activityService.logActivity(user, "Edit user: " + user.getUsername());
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        User user = doFindById(userId);

        user.setStatus(Status.DELETED);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void suspendUser(UUID userId) {
        User user = doFindById(userId);

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
            throw new RuntimeException("User with ID " + userId + NOT_FOUND);
        }
    }

    @Transactional
    @Override
    public void incrementFailedLoginAttempts(String username) {
        User user = this.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + NOT_FOUND));

        user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<User> search(String term, int page, int size) {
        if (term == null || term.trim().isEmpty()) {
            // Empty term → return first page of all users ordered by username ascending
            Pageable pageable = PageRequest.of(page, size, Sort.by("username").ascending());
            return userRepository.findAll(pageable);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("username").ascending());
        return userRepository.searchUsers(term.trim(), pageable);
    }

    @Override
    public long getTotalUsers() {
        return userRepository.count();
    }

    @Override
    public long getSuspendedUsers() {
        return userRepository.countByStatus(Status.SUSPENDED);
    }

    @Override
    public long getActiveUsers() {
        return userRepository.countByStatus(Status.ACTIVE);
    }

    @Override
    public long getDeletedUsers() {
        return userRepository.countByStatus(Status.DELETED);
    }


    // === MÉTODOS PRIVADOS ===

    private User doFindById(UUID userId) {
        return userRepository.findWithActivitiesById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + NOT_FOUND));
    }

    private void updateUsername(User user, String newUsername) {
        if (newUsername == null || newUsername.equals(user.getUsername())) return;

        if (newUsername.length() < 7) {
            throw new IllegalArgumentException("Username must be at least 7 characters long");
        }

        if (userRepository.findByUsername(newUsername).isPresent()) {
            throw new UsernameAlreadyExistsException("Username is already in use");
        }

        user.setUsername(newUsername);
    }

    private void updateEmail(User user, String newEmail) {
        if (newEmail == null || newEmail.equals(user.getEmail())) return;

        if (userRepository.findByEmail(newEmail).isPresent()) {
            throw new EmailAlreadyExistsException("Email is already in use");
        }

        user.setEmail(newEmail);
    }

    private void updateName(User user, String newName) {
        if (newName != null && !newName.equals(user.getName())) {
            user.setName(newName);
        }
    }

    private void updateRole(User user, Role newRole) {
        if (newRole != null && !newRole.equals(user.getRole())) {
            user.setRole(newRole);
        }
    }

    private void updateStatus(User user, Status newStatus) {
        if (newStatus != null && !newStatus.equals(user.getStatus())) {
            user.setStatus(newStatus);
        }
    }
}



