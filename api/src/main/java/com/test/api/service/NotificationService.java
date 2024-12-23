package com.test.api.service;

import com.test.api.dto.UserActionMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendUserActionNotification(String user, String action){
        UserActionMessageDto message = new UserActionMessageDto();
        message.setUser(user);
        message.setAction(action);
        messagingTemplate.convertAndSend("/userActionNotification/all", message);

    }
}
