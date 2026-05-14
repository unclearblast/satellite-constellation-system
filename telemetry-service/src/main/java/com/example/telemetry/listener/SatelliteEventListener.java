package com.example.telemetry.listener;

import com.example.telemetry.dto.SatelliteEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SatelliteEventListener {

    private static final Logger log = LoggerFactory.getLogger(SatelliteEventListener.class);

    @KafkaListener(topics = "satellite-events", groupId = "telemetry-group")
    public void handleSatelliteEvent(SatelliteEvent event) {
        log.info("Received satellite event: {}", event);
        switch (event.getEventType()) {
            case CREATED:
                // Здесь можно обновить локальный кэш или БД
                log.info("Satellite {} ({}) has been CREATED", event.getSatelliteId(), event.getSatelliteName());
                break;
            case DELETED:
                log.info("Satellite {} ({}) has been DELETED", event.getSatelliteId(), event.getSatelliteName());
                break;
        }
    }
}
