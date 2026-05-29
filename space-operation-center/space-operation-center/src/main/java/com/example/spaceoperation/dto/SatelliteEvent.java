package com.example.spaceoperation.dto;

import java.time.Instant;
import java.util.UUID;

public class SatelliteEvent {
    private String eventId;
    private EventType eventType;
    private String satelliteId;
    private String satelliteName;
    private Instant timestamp;

    public SatelliteEvent() {}

    public SatelliteEvent(EventType eventType, String satelliteId, String satelliteName) {
        this.eventId = UUID.randomUUID().toString();
        this.eventType = eventType;
        this.satelliteId = satelliteId;
        this.satelliteName = satelliteName;
        this.timestamp = Instant.now();
    }

    // Getters and setters
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public EventType getEventType() { return eventType; }
    public void setEventType(EventType eventType) { this.eventType = eventType; }
    public String getSatelliteId() { return satelliteId; }
    public void setSatelliteId(String satelliteId) { this.satelliteId = satelliteId; }
    public String getSatelliteName() { return satelliteName; }
    public void setSatelliteName(String satelliteName) { this.satelliteName = satelliteName; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public enum EventType {
        CREATED, DELETED
    }
}
