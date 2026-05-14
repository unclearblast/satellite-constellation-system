package com.example.spaceoperation.service;

import com.example.spaceoperation.dto.SatelliteEvent;
import com.example.spaceoperation.entity.Satellite;
import com.example.spaceoperation.repository.SatelliteRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SatelliteService {

    private final SatelliteRepository repository;
    private final KafkaTemplate<String, SatelliteEvent> kafkaTemplate;

    // Inject KafkaTemplate в конструктор
    public SatelliteService(SatelliteRepository repository,
                            KafkaTemplate<String, SatelliteEvent> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public Satellite createSatellite(String id, String name) {
        Satellite satellite = new Satellite(id, name);
        Satellite saved = repository.save(satellite);
        sendEvent(SatelliteEvent.EventType.CREATED, saved);
        return saved;
    }

    @Transactional
    public void deleteSatellite(String id) {
        Satellite satellite = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Satellite not found"));
        repository.delete(satellite);
        sendEvent(SatelliteEvent.EventType.DELETED, satellite);
    }

    private void sendEvent(SatelliteEvent.EventType type, Satellite satellite) {
        SatelliteEvent event = new SatelliteEvent(type, satellite.getId(), satellite.getName());
        kafkaTemplate.send("satellite-events", satellite.getId(), event);
    }
}
