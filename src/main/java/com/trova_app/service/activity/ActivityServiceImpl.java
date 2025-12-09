package com.trova_app.service.activity;

import com.trova_app.model.Activity;
import com.trova_app.model.User;
import com.trova_app.repository.ActivityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityServiceImpl(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public void logActivity(User user, String description) {
        Activity activity = new Activity();
        activity.setUser(user);
        activity.setDescription(description);
        activity.setTimestamp(LocalDateTime.now());

        activityRepository.save(activity);
    }
}
