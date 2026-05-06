package com.example.missionscheduler.dto;

import com.example.missionscheduler.domain.TargetType;

public record MissionRequest(
        TargetType targetType,
        String constellationName,
        String satelliteName
) {}
