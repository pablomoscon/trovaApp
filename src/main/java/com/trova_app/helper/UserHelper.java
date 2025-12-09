package com.trova_app.helper;

import com.trova_app.exception.UserNotFoundException;
import com.trova_app.model.User;
import com.trova_app.security.UserPrincipal;
import com.trova_app.service.activity.ActivityService;
import com.trova_app.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserHelper {

    private final UserService userService;
    private final ActivityService activityService;

    @Autowired
    public UserHelper(UserService userService, ActivityService activityService) {
        this.userService = userService;
        this.activityService = activityService;
    }

    public User getAuthenticatedUser() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return userService.findById(userPrincipal.getId());
    }

    public void logActivity(String action) {
        User user = getAuthenticatedUser();
        activityService.logActivity(user, action);
    }
}
