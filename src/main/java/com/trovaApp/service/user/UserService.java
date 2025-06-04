package com.trovaApp.service.user;

import com.trovaApp.dto.user.UserPatchDTO;
import com.trovaApp.dto.user.UserSignupDTO;
import com.trovaApp.enums.Role;
import com.trovaApp.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User save(UserSignupDTO userDto);

    List<User> findAll();

    @Transactional(readOnly = true)
    User findByIdWithActivities(UUID userId);

    User findById(UUID id);

    Optional<User> findByUsername(String username);

    void changeRole (Role newRole, String username);

    @Transactional
    User patchUser(UUID userId, UserPatchDTO userPatchDTO);

    void delete(UUID id);

    void suspendUser(UUID id);

    void updateLastLogin(UUID userId);

    @Transactional
    void incrementFailedLoginAttempts(String username);

}
