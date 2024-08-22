package com.test.api.service;

import com.test.api.dto.UserActionMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotification(String path, String user, String action){

        UserActionMessageDto message = new UserActionMessageDto();
        message.setUser(user);
        message.setAction(action);
        messagingTemplate.convertAndSend(path, message);
        log.info("requiter: {}, action: {}", user, action);


    }
}
