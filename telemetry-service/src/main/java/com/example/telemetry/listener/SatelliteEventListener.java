package com.example.telemetry.listener;

import com.example.telemetry.domain.inbox.Inbox;
import com.example.telemetry.dto.SatelliteEvent;
import com.example.telemetry.repository.InboxRepository;
import com.example.telemetry.service.SatelliteStorageService; // ваш сервис хранения спутников
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SatelliteEventListener {

    private final InboxRepository inboxRepository;
    private final SatelliteStorageService satelliteStorageService;

    @KafkaListener(topics = "satellite-events", groupId = "telemetry-group")
    @Transactional
    public void handleSatelliteEvent(SatelliteEvent event) {
        // Идемпотентность: проверяем, не обработано ли событие
        if (inboxRepository.existsById(event.getEventId())) {
            log.info("Duplicate event {} ignored", event.getEventId());
            return;
        }

        // Атомарно сохраняем в Inbox и выполняем бизнес-логику
        Inbox inboxRecord = new Inbox(event.getEventId(), event.getAggregateId(), event.getEventType());
        inboxRepository.save(inboxRecord);

        // Выполняем действие в зависимости от типа события
        switch (event.getEventType()) {
            case "CREATED" -> satelliteStorageService.addSatellite(event.getAggregateId());
            case "DELETED" -> satelliteStorageService.removeSatellite(event.getAggregateId());
            default -> log.warn("Unknown event type: {}", event.getEventType());
        }

        log.info("Processed event {} for satellite {}", event.getEventId(), event.getAggregateId());
    }
}
