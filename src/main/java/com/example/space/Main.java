package com.example.space;

import com.example.space.domain.satellite.*;
import com.example.space.repository.ConstellationRepository;
import com.example.space.service.SpaceOperationCenterService;

public class Main {

    public static void main(String[] args) {

        ConstellationRepository repository = new ConstellationRepository();
        SpaceOperationCenterService service =
                new SpaceOperationCenterService(repository);

        System.out.println("ЗАПУСК СИСТЕМЫ УПРАВЛЕНИЯ СПУТНИКОВОЙ ГРУППИРОВКОЙ");
        System.out.println("============================================================");

        Satellite comm1 = service.getCommunicationFactory().createSatellite(
                "Связь-1",
                EnergySystem.builder().batteryLevel(0.85).build(),
                500
        );

        Satellite comm2 = service.getCommunicationFactory().createSatellite(
                "Связь-2",
                EnergySystem.builder().batteryLevel(0.75).build(),
                1000
        );

        Satellite img1 = service.getImagingFactory().createSatellite(
                "ДЗЗ-1",
                EnergySystem.builder().batteryLevel(0.92).build(),
                2.5
        );

        Satellite img2 = service.getImagingFactory().createSatellite(
                "ДЗЗ-2",
                EnergySystem.builder().batteryLevel(0.45).build(),
                1.0
        );

        Satellite img3 = service.getImagingFactory().createSatellite(
                "ДЗЗ-3",
                EnergySystem.builder().batteryLevel(0.15).build(),
                0.5
        );

        service.createAndSaveConstellation("Орбита-1");
        service.createAndSaveConstellation("Орбита-2");

        service.addSatelliteToConstellation("Орбита-1", comm1);
        service.addSatelliteToConstellation("Орбита-1", img1);
        service.addSatelliteToConstellation("Орбита-1", img2);

        service.addSatelliteToConstellation("Орбита-2", comm2);
        service.addSatelliteToConstellation("Орбита-2", img3);

        service.activateAllSatellites("Орбита-1");
        service.executeConstellationMission("Орбита-1");
        service.showConstellationStatus("Орбита-1");

        System.out.println(service.getAllConstellations());
    }
}
