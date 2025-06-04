package com.trovaApp.service.activity;

import com.trovaApp.model.User;

public interface ActivityService {
    void logActivity(User user, String description);
}