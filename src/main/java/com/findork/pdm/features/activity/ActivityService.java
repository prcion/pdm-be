package com.findork.pdm.features.activity;

import com.findork.pdm.features.account.User;
import com.findork.pdm.features.account.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    @Transactional
    public void save(Activity activity, User user) {
        user.addActivity(activity);
        userRepository.save(user);
    }

    public List<Activity> getAll() {
        return activityRepository.findAll();
    }

    public Activity save(Activity activity) {
        User user = userRepository.getOne(2L);
        activity.setUser(user);
        return activityRepository.save(activity);
//        return activity;
    }

    public Activity update(Long id, String name) {

        Activity activityDb = activityRepository.getOne(id);
        activityDb.setName(name);
        return activityRepository.save(activityDb);
    }

}
