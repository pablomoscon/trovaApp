package com.trova_app.service.activity;

import com.trova_app.model.User;

public interface ActivityService {
    void logActivity(User user, String description);
}