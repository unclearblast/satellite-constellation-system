package com.example.space.service;

import com.example.space.domain.constellation.SatelliteConstellation;
import com.example.space.domain.satellite.Satellite;
import com.example.space.repository.ConstellationRepository;

import java.util.Map;

public class SpaceOperationCenterService {

    private final ConstellationRepository repository;

    public SpaceOperationCenterService(ConstellationRepository repository) {
        this.repository = repository;
    }

    public void createAndSaveConstellation(String name) {

        SatelliteConstellation constellation =
                new SatelliteConstellation(name);

        repository.save(name, constellation);

        System.out.println("Создана и сохранена группировка: " + name);
    }

    public void addSatelliteToConstellation(String name, Satellite satellite) {

        repository.findByName(name).addSatellite(satellite);
    }

    public void activateAllSatellites(String name) {

        System.out.println("Активация всех спутников группировки: " + name);
        repository.findByName(name).activateAll();
    }

    public void executeConstellationMission(String name) {

        System.out.println("Запуск миссии группировки: " + name);
        repository.findByName(name).executeMissions();
    }

    public void showConstellationStatus(String name) {

        System.out.println(repository.findByName(name));
    }

    public Map<String, SatelliteConstellation> getAllConstellations() {
        return repository.findAll();
    }
}