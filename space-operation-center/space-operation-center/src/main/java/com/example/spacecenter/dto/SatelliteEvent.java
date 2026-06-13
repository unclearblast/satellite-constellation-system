package com.example.spacecenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SatelliteEvent {
    private String eventId;      // уникальный ID события (можно UUID)
    private String aggregateId;  // ID спутника
    private String eventType;    // "CREATED" или "DELETED"
    private Object payload;      // данные спутника (JSON)
}
