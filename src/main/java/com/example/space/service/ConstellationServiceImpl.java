package com.example.space.service;

import com.example.space.domain.constellation.SatelliteConstellation;
import com.example.space.domain.satellite.Satellite;
import com.example.space.repository.ConstellationRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ConstellationServiceImpl implements ConstellationService {

    private final ConstellationRepository repository;

    public ConstellationServiceImpl(ConstellationRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createConstellation(String name) {

        repository.save(name, new SatelliteConstellation(name));

        System.out.println("Создана и сохранена группировка: " + name);
    }

    @Override
    public void addSatellite(String constellationName, Satellite satellite) {
        repository.findByName(constellationName).addSatellite(satellite);
    }

    @Override
    public void activateAll(String constellationName) {
        repository.findByName(constellationName).activateAll();
    }

    @Override
    public void executeMission(String constellationName) {
        repository.findByName(constellationName).executeMissions();
    }

    @Override
    public Map<String, SatelliteConstellation> getAllConstellations() {
        return repository.findAll();
    }
}
