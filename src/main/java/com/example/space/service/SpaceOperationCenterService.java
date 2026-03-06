package com.example.space.service;

import com.example.space.domain.constellation.SatelliteConstellation;
import com.example.space.domain.satellite.Satellite;
import com.example.space.param.SatelliteParam;
import com.example.space.repository.ConstellationRepository;

import java.util.Map;

public class SpaceOperationCenterService {

    private final ConstellationRepository repository;
    private final SatelliteService satelliteService;
    private final ConstellationService constellationService;

    public SpaceOperationCenterService(
            ConstellationRepository repository,
            SatelliteService satelliteService,
            ConstellationService constellationService
    ) {
        this.repository = repository;
        this.satelliteService = satelliteService;
        this.constellationService = constellationService;
    }

    public void createAndSaveConstellation(String name) {

        SatelliteConstellation constellation =
                constellationService.createConstellation(name);

        repository.save(name, constellation);

        System.out.println("Создана и сохранена группировка: " + name);
    }

    public void addSatelliteToConstellation(String name, Satellite satellite) {

        repository.findByName(name).addSatellite(satellite);
    }

    public Satellite addSatellite(SatelliteParam param, String constellationName) {

        Satellite satellite = satelliteService.createSatellite(param);

        repository.findByName(constellationName).addSatellite(satellite);

        return satellite;
    }

    public void activateAllSatellites(String name) {

        repository.findByName(name).activateAll();
    }

    public void executeConstellationMission(String name) {

        repository.findByName(name).executeMissions();
    }

    public void showConstellationStatus(String name) {

        System.out.println(repository.findByName(name));
    }

    public Map<String, SatelliteConstellation> getAllConstellations() {

        return repository.findAll();
    }

    public void executeMission(MissionRequest missionRequest) {

        System.out.println("============================================================");
        System.out.println("🚀 ЗАПУСК МИССИИ");
        System.out.println("============================================================");

        SatelliteConstellation constellation =
                repository.findByName(missionRequest.getConstellationName());

        constellation.activateAll();
        constellation.executeMissions();
    }
}
