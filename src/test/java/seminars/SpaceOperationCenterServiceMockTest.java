package seminars;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import seminars.domain.constellation.SatelliteConstellation;
import seminars.domain.satellite.CommunicationSatellite;
import seminars.domain.satellite.EnergySystem;
import seminars.domain.satellite.SatelliteState;
import seminars.repository.ConstellationRepository;
import seminars.service.SpaceOperationCenterService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Mock tests для SpaceOperationCenterService")
class SpaceOperationCenterServiceMockTest {

    private static final String CONSTELLATION_NAME = "Орбита-Service-Mock";

    @Mock
    private ConstellationRepository repository;

    @InjectMocks
    private SpaceOperationCenterService service;

    private SatelliteConstellation constellation;

    @BeforeEach
    void setup() {
        constellation = new SatelliteConstellation(CONSTELLATION_NAME);
    }

    @Test
    @DisplayName("Создание и сохранение группировки")
    void createAndSaveConstellation_shouldCallRepositorySave() {
        service.createAndSaveConstellation(CONSTELLATION_NAME);
        verify(repository, times(1))
                .save(eq(CONSTELLATION_NAME), any(SatelliteConstellation.class));
    }

    @Test
    @DisplayName("Добавление спутника в группировку")
    void addSatelliteToConstellation_shouldAddSatellite() {
        when(repository.findByName(CONSTELLATION_NAME)).thenReturn(constellation);

        CommunicationSatellite satellite = new CommunicationSatellite(
                "Связь-Мок",
                300,
                new EnergySystem(0.8),
                new SatelliteState()
        );

        service.addSatelliteToConstellation(CONSTELLATION_NAME, satellite);

        assertEquals(1, constellation.getSatellites().size());
        verify(repository, times(1)).findByName(CONSTELLATION_NAME);
    }

    @Test
    @DisplayName("Активация всех спутников")
    void activateAllSatellites_shouldActivateSatellites() {
        when(repository.findByName(CONSTELLATION_NAME)).thenReturn(constellation);

        CommunicationSatellite satellite = new CommunicationSatellite(
                "Связь-Активация",
                200,
                new EnergySystem(0.9),
                new SatelliteState()
        );
        constellation.addSatellite(satellite);

        service.activateAllSatellites(CONSTELLATION_NAME);

        assertEquals(true, satellite.getState().isActive());
    }
}
