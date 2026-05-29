package com.example.telemetry.domain.inbox;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "inbox")
@Getter
@Setter
@NoArgsConstructor
public class Inbox {

    @Id
    @Column(name = "event_id")
    private String eventId;

    @Column(name = "aggregate_id", nullable = false)
    private String aggregateId;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "processed_at", nullable = false)
    private Instant processedAt = Instant.now();

    public Inbox(String eventId, String aggregateId, String eventType) {
        this.eventId = eventId;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
    }
}
