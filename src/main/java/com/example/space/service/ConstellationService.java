package com.example.space.service;

import com.example.space.domain.constellation.SatelliteConstellation;
import com.example.space.domain.satellite.Satellite;

import java.util.Map;

public interface ConstellationService {

    void createConstellation(String name);

    void addSatellite(String constellationName, Satellite satellite);

    void activateAll(String constellationName);

    void executeMission(String constellationName);

    Map<String, SatelliteConstellation> getAllConstellations();
}
