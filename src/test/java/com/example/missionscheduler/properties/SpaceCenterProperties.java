package com.example.missionscheduler.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app.space-center-service")
public record SpaceCenterProperties(
        String url,
        List<MissionConfig> missions
) {}
