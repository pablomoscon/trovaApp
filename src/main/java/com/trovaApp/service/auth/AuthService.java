package com.trovaApp.service.auth;

import com.trovaApp.dto.SigninUserDto;
import com.trovaApp.model.User;

public interface AuthService {
    User signInAndReturnJwt(SigninUserDto signInRequest);
}
