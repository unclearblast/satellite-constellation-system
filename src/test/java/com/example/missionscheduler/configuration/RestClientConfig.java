package com.example.missionscheduler.configuration;

import com.example.missionscheduler.properties.SpaceCenterProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient spaceOperationRestClient(SpaceCenterProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.url())
                .build();
    }
}
