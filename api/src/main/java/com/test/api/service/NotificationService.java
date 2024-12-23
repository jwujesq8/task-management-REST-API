package com.test.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Warsaw"));

    public void sendUserActionNotification(String user, String action){
        messagingTemplate.convertAndSend("/userActionNotification/all",
                calendar.getTime() + " : " + user + " - " + action);

    }
}
