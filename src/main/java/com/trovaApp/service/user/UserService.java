package com.trovaApp.service.user;

import com.trovaApp.dto.user.UserSignupDTO;
import com.trovaApp.enums.Role;
import com.trovaApp.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(UserSignupDTO userDto);

    List<User> findAll();

    Optional<User> findByUsername(String username);

    void changeRole (Role newRole, String username);
}
