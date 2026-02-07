package seminars;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import seminars.domain.satellite.CommunicationSatellite;
import seminars.domain.satellite.EnergySystem;
import seminars.domain.satellite.ImagingSatellite;
import seminars.domain.satellite.SatelliteState;
import seminars.service.SpaceOperationCenterService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Integration tests для SpaceOperationCenterService")
class SpaceOperationCenterServiceIntegrationTest {

    @Autowired
    private SpaceOperationCenterService service;

    @Test
    @DisplayName("Полный жизненный цикл через сервис")
    void fullLifecycleTest() {
        String constellationName = "Орбита-Service-Integration";

        service.createAndSaveConstellation(constellationName);

        CommunicationSatellite communicationSatellite =
                new CommunicationSatellite(
                        "Связь-INT",
                        500,
                        new EnergySystem(0.85),
                        new SatelliteState()
                );

        ImagingSatellite imagingSatellite =
                new ImagingSatellite(
                        "ДЗЗ-INT",
                        1.2,
                        new EnergySystem(0.9),
                        new SatelliteState()
                );

        service.addSatelliteToConstellation(constellationName, communicationSatellite);
        service.addSatelliteToConstellation(constellationName, imagingSatellite);

        service.activateAllSatellites(constellationName);

        assertTrue(communicationSatellite.getState().isActive());
        assertTrue(imagingSatellite.getState().isActive());

        double energyBefore = communicationSatellite.getEnergy().getBatteryLevel();

        service.executeConstellationMission(constellationName);

        assertTrue(
                communicationSatellite.getEnergy().getBatteryLevel() < energyBefore
        );

        assertEquals(
                1,
                imagingSatellite.getPhotosTaken()
        );
    }
}
