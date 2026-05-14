package com.example.missionscheduler.service;

import com.example.missionscheduler.client.SpaceOperationClient;
import com.example.missionscheduler.dto.MissionRequest;
import com.example.missionscheduler.properties.MissionConfig;
import com.example.missionscheduler.properties.SpaceCenterProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfiguredMissionScheduler {

    private final SpaceCenterProperties properties;
    private final SpaceOperationClient client;
    private final TaskScheduler taskScheduler;

    @PostConstruct
    public void scheduleMissions() {
        for (MissionConfig mission : properties.missions()) {
            scheduleMission(mission);
        }
    }

    private void scheduleMission(MissionConfig config) {

        validate(config);

        Runnable task = () -> {
            try {
                log.info("Executing mission: {}", config);

                MissionRequest request = new MissionRequest(
                        config.targetType(),
                        config.constellationName(),
                        config.satelliteName()
                );

                client.executeMission(request);

                log.info("Mission success: {}", config);

            } catch (Exception e) {
                log.error("Mission failed: {}", config, e);
            }
        };

        taskScheduler.schedule(task, new CronTrigger(config.cron()));
    }

    private void validate(MissionConfig config) {
        if (config.targetType() == null) {
            throw new IllegalArgumentException("targetType is required");
        }

        if (config.constellationName() == null) {
            throw new IllegalArgumentException("constellationName is required");
        }

        if (config.targetType().name().equals("SINGLE_SATELLITE")
                && config.satelliteName() == null) {
            throw new IllegalArgumentException("satelliteName required for SINGLE_SATELLITE");
        }
    }
}
