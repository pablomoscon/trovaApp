package com.trovaApp.service.user;

import com.trovaApp.dto.user.UserPatchDTO;
import com.trovaApp.dto.user.UserSignupDTO;
import com.trovaApp.enums.Role;
import com.trovaApp.model.User;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User save(UserSignupDTO userDto);

    List<User> findAll();

    Optional <User> findById(UUID id);

    Optional<User> findByUsername(String username);

    void changeRole (Role newRole, String username);

    @Transactional
    User patchUser(UUID userId, UserPatchDTO userPatchDTO);
}
