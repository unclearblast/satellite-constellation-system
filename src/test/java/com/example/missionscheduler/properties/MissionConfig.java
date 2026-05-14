package com.example.missionscheduler.properties;

import com.example.missionscheduler.domain.TargetType;

public record MissionConfig(
        TargetType targetType,
        String constellationName,
        String satelliteName,
        String cron
) {}
