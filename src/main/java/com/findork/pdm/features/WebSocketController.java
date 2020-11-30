package com.findork.pdm.features;

import com.findork.pdm.features.account.User;
import com.findork.pdm.features.activity.ActivityDtoWs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class WebSocketController {

    @MessageMapping("/news")
    @SendTo("/topic/activity")
    public ResponseEntity<?> broadcastNews(@Payload ActivityDtoWs activityDto, User user) {
        System.out.println(user.getId());
        return ResponseEntity.ok(activityDto);
    }
}
