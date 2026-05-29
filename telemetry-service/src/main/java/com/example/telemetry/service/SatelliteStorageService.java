package com.example.telemetry.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@Slf4j
public class SatelliteStorageService {
    private final ConcurrentMap<String, Boolean> satellites = new ConcurrentHashMap<>();

    public void addSatellite(String id) {
        satellites.put(id, true);
        log.info("Satellite {} added to telemetry storage", id);
    }

    public void removeSatellite(String id) {
        satellites.remove(id);
        log.info("Satellite {} removed from telemetry storage", id);
    }

    public boolean exists(String id) {
        return satellites.containsKey(id);
    }
}
