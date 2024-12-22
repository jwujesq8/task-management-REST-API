package com.test.api.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.annotation.PreDestroy;

@Configuration
@ConfigurationProperties(prefix = "user-rest-api")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class ShutdownTimeProperty {

    private String shutdownTime;

}

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

