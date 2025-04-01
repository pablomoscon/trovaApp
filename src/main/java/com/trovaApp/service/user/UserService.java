package com.trovaApp.service.user;

import com.trovaApp.dto.SignupUserDto;
import com.trovaApp.enums.Role;
import com.trovaApp.model.User;

import java.util.Optional;

public interface UserService {
    User save(SignupUserDto userDto);

    Optional<User> findByUsername(String username);

    void changeRole (Role newRole, String username);
}
