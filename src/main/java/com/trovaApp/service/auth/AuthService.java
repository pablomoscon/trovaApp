package com.trovaApp.service.auth;

import com.trovaApp.dto.user.UserSigninDTO;
import com.trovaApp.model.User;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    User signInAndReturnJwt(UserSigninDTO signInRequest, HttpServletRequest request);
}
