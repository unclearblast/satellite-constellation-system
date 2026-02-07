package seminars;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import seminars.domain.constellation.SatelliteConstellation;
import seminars.domain.satellite.CommunicationSatellite;
import seminars.domain.satellite.EnergySystem;
import seminars.domain.satellite.SatelliteState;
import seminars.repository.ConstellationRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Integration tests для ConstellationRepository")
class ConstellationRepositoryIntegrationTest {

    @Autowired
    private ConstellationRepository repository;

    @Test
    @DisplayName("Полный жизненный цикл группировки")
    void testFullLifecycle() {
        String name = "Орбита-Integration";
        SatelliteConstellation constellation = new SatelliteConstellation(name);
        repository.save(name, constellation);

        CommunicationSatellite sat = new CommunicationSatellite(
                "Связь-Int", 100, new EnergySystem(0.9), new SatelliteState()
        );
        constellation.addSatellite(sat);

        constellation.activateAll();
        assertTrue(sat.getState().isActive());

        constellation.executeMissions();
        assertTrue(sat.getEnergy().getBatteryLevel() < 0.9);

        SatelliteConstellation found = repository.findByName(name);
        assertNotNull(found);
        assertEquals(1, found.getSatellites().size());
    }
}
