package com.test.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.annotation.PreDestroy;

@Component
public class ShutdownScheduler {

    @Autowired
    private ShutdownTimeProperty ShutdownTimeProperty;

    @Autowired
    private ApplicationContext context;

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Scheduled(fixedRate = 600000) // 10 minutes in milliseconds
    public void checkShutdownTime() {
        String shutdownTimeStr = ShutdownTimeProperty.getShutdownTime();
        LocalDateTime shutdownTime = LocalDateTime.parse(shutdownTimeStr, formatter);
        LocalDateTime currentTime = LocalDateTime.now();

        if (currentTime.isAfter(shutdownTime)) {
            shutdown();
        }
    }

    @PreDestroy
    public void onShutdown() {
        System.out.println("Application is shutting down...");
    }

    private void shutdown() {
        System.out.println("Shutdown time exceeded. Closing application...");
        ConfigurableApplicationContext configurableApplicationContext =
                (org.springframework.context.ConfigurableApplicationContext) context;
        configurableApplicationContext.close();
    }
}

