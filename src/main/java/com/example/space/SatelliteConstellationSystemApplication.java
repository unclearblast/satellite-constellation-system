package com.example.space;

import com.example.space.domain.constellation.SatelliteConstellation;
import com.example.space.domain.satellite.Satellite;
import com.example.space.param.CommunicationSatelliteParam;
import com.example.space.param.ImagingSatelliteParam;
import com.example.space.repository.ConstellationRepository;
import com.example.space.service.SatelliteService;
import com.example.space.service.SpaceOperationCenterService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SatelliteConstellationSystemApplication implements CommandLineRunner {

    private final SatelliteService satelliteService;

    public SatelliteConstellationSystemApplication(SatelliteService satelliteService) {
        this.satelliteService = satelliteService;
    }

    @Override
    public void run(String... args) {

        System.out.println("============================================================");
        System.out.println("======== ЗАПУСК СПУТНИКОВОЙ СИСТЕМЫ УПРАВЛЕНИЯ ==========");
        System.out.println("============================================================");

        ConstellationRepository repository = new ConstellationRepository();
        SpaceOperationCenterService service =
                new SpaceOperationCenterService(repository);

        Satellite comm1 = satelliteService.createSatellite(
                new CommunicationSatelliteParam("Связь-1", 0.85, 500)
        );

        Satellite comm2 = satelliteService.createSatellite(
                new CommunicationSatelliteParam("Связь-2", 0.75, 1000)
        );

        Satellite img1 = satelliteService.createSatellite(
                new ImagingSatelliteParam("ДЗЗ-1", 0.92, 2.5)
        );

        Satellite img2 = satelliteService.createSatellite(
                new ImagingSatelliteParam("ДЗЗ-2", 0.45, 1.0)
        );

        Satellite img3 = satelliteService.createSatellite(
                new ImagingSatelliteParam("ДЗЗ-3", 0.15, 0.5)
        );

        service.createAndSaveConstellation("Орбита-1");
        service.createAndSaveConstellation("Орбита-2");

        service.addSatelliteToConstellation("Орбита-1", comm1);
        service.addSatelliteToConstellation("Орбита-1", img1);
        service.addSatelliteToConstellation("Орбита-1", img2);

        service.addSatelliteToConstellation("Орбита-2", comm2);
        service.addSatelliteToConstellation("Орбита-2", img3);

        System.out.println("------------------------------------------------------------");
        System.out.println("АКТИВАЦИЯ СПУТНИКОВ ГРУППИРОВКИ Орбита-1");
        System.out.println("------------------------------------------------------------");

        service.activateAllSatellites("Орбита-1");

        System.out.println("------------------------------------------------------------");
        System.out.println("ВЫПОЛНЕНИЕ МИССИИ");
        System.out.println("------------------------------------------------------------");

        service.executeConstellationMission("Орбита-1");

        System.out.println("------------------------------------------------------------");
        System.out.println("ТЕКУЩЕЕ СОСТОЯНИЕ ГРУППИРОВКИ");
        System.out.println("------------------------------------------------------------");

        service.showConstellationStatus("Орбита-1");

        System.out.println("============================================================");
        System.out.println("======== ЗАВЕРШЕНИЕ РАБОТЫ СИСТЕМЫ ========================");
        System.out.println("============================================================");
    }
}
