package com.trova_app.service.auth;

import com.trova_app.dto.user.UserSigninDTO;
import com.trova_app.model.User;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    User signInAndReturnJwt(UserSigninDTO signInRequest, HttpServletRequest request);
}
