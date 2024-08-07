package com.test.api.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "test-app")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShutdownTimeProperty {

    private String shutdownTime;

}

