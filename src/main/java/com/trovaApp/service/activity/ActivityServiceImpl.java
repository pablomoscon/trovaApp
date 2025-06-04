package com.trovaApp.service.activity;

import com.trovaApp.model.Activity;
import com.trovaApp.model.User;
import com.trovaApp.repository.ActivityRepository;
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
