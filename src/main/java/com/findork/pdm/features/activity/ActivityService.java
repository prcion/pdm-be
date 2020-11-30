package com.findork.pdm.features.activity;

import com.findork.pdm.features.account.User;
import com.findork.pdm.features.account.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    public Page<Activity> searchByUserId(Long userId, Pageable pageable, String name) {
        return activityRepository.findAllByUserId(userId, pageable, name);
    }

    public Activity save(Activity activity, User currentUser) {
        User user = userRepository.getOne(currentUser.getId());
        activity.setUser(user);
        return activityRepository.save(activity);
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
