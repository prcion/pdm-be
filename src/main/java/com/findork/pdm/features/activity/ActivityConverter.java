package com.findork.pdm.features.activity;

import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ActivityConverter {

    public ActivityDto fromActivityToActivityDto(Activity activity) {
        return ActivityDto.builder().id(activity.getId())
                .name(activity.getName())
                .createdDate(activity.getCreatedDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .updatedDate(activity.getUpdatedDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .userId(activity.getUser().getId()).build();
    }

    public Activity fromActivityDtoToActivity(ActivityDto activityDto) {
        return Activity.builder().name(activityDto.getName()).build();
    }

    public List<ActivityDto> fromListActivityToListActivityDto(List<Activity> activities) {
        return activities.stream().map(this::fromActivityToActivityDto).collect(Collectors.toList());
    }

    public List<Activity> fromListActivityDtoToListActivity(List<ActivityDto> activityDtos) {
        return activityDtos.stream().map(this::fromActivityDtoToActivity).collect(Collectors.toList());
    }
}
