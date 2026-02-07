package seminars;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import seminars.domain.satellite.*;
import seminars.service.SpaceOperationCenterService;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {

        ConfigurableApplicationContext context =
                SpringApplication.run(Main.class, args);

        SpaceOperationCenterService service =
                context.getBean(SpaceOperationCenterService.class);

        System.out.println("ЗАПУСК СИСТЕМЫ УПРАВЛЕНИЯ СПУТНИКОВОЙ ГРУППИРОВКОЙ");
        System.out.println("============================================================");

        Satellite comm1 = new CommunicationSatellite(
                "Связь-1", 500,
                new EnergySystem(0.85),
                new SatelliteState()
        );

        Satellite comm2 = new CommunicationSatellite(
                "Связь-2", 1000,
                new EnergySystem(0.75),
                new SatelliteState()
        );

        Satellite img1 = new ImagingSatellite(
                "ДЗЗ-1", 2.5,
                new EnergySystem(0.92),
                new SatelliteState()
        );

        Satellite img2 = new ImagingSatellite(
                "ДЗЗ-2", 1.0,
                new EnergySystem(0.45),
                new SatelliteState()
        );

        Satellite img3 = new ImagingSatellite(
                "ДЗЗ-3", 0.5,
                new EnergySystem(0.15),
                new SatelliteState()
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
