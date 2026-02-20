package com.example.space.repository;

import com.example.space.domain.constellation.SatelliteConstellation;

import java.util.HashMap;
import java.util.Map;

public class ConstellationRepository {

    private final Map<String, SatelliteConstellation> storage = new HashMap<>();

    public void save(String name, SatelliteConstellation constellation) {
        storage.put(name, constellation);
    }

    public SatelliteConstellation findByName(String name) {
        return storage.get(name);
    }

    public Map<String, SatelliteConstellation> findAll() {
        return storage;
    }
}
