package com.findork.pdm.features.activity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.findork.pdm.config.CustomStompSessionHandler;
import com.findork.pdm.features.account.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.Comparator;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityConverter activityConverter;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<?> getAllActivitiesByUser(User user) {
        System.out.println(user.getUsername());
        return ResponseEntity.ok(activityConverter.fromListActivityToListActivityDto(user.getActivities()));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllActivities() {
        return ResponseEntity.ok(activityConverter.fromListActivityToListActivityDto(activityService.getAll()).stream().sorted(Comparator.comparing(ActivityDto::getId).reversed()).collect(Collectors.toList()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateActivity(@RequestBody ActivityDto activityDto) throws ExecutionException, InterruptedException {
        StompSession stompSession = getStompSession();
        activityDto = activityConverter.fromActivityToActivityDto(activityService.update(activityDto.getId(), activityDto.getName()));
        stompSession.send("/app/news", new ActivityDtoWs( "updated", activityDto));
        return ResponseEntity.ok(activityDto);
    }

    @PostMapping
    public void createActivityForUser(@RequestBody ActivityDto activityDto, User user) {
        var activity = activityConverter.fromActivityDtoToActivity(activityDto);
        activityService.save(activity, user);
    }

    @PostMapping("/test")
    public ResponseEntity<?> createActivityTestForUser(@RequestBody ActivityDto activityDto) throws ExecutionException, InterruptedException {
        StompSession stompSession = getStompSession();

        var activity = activityService.save(activityConverter.fromActivityDtoToActivity(activityDto));
        activityDto = activityConverter.fromActivityToActivityDto(activity);
        stompSession.send("/app/news", new ActivityDtoWs( "created", activityDto));
        return ResponseEntity.ok(activityDto);
    }


    private StompSession getStompSession() throws ExecutionException, InterruptedException {
        StompSessionHandler sessionHandler = new CustomStompSessionHandler();
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = stompClient.connect("ws://localhost:8080/ws",
                sessionHandler).get();
        return stompSession;
    }
}
