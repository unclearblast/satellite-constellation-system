// test/.../repository/SatelliteRepositoryTest.java
package com.example.spacecenter.repository;

import com.example.spacecenter.domain.constellation.SatelliteConstellation;
import com.example.spacecenter.domain.energy.EnergySystem;
import com.example.spacecenter.domain.satellite.CommunicationSatellite;
import com.example.spacecenter.domain.satellite.SatelliteState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SatelliteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SatelliteRepository satelliteRepository;

    @Test
    void shouldSaveAndFindSatellite() {
        // given
        EnergySystem energy = new EnergySystem();
        energy.setPowerCapacityWatts(1500.0);
        energy.setCurrentPowerWatts(1200.0);
        energy.setStatus(EnergySystem.EnergySystemStatus.NOMINAL);
        entityManager.persist(energy);

        SatelliteConstellation constellation = new SatelliteConstellation();
        constellation.setName("Starlink");
        entityManager.persist(constellation);

        CommunicationSatellite sat = new CommunicationSatellite();
        sat.setName("Sat-001");
        sat.setLaunchDate(LocalDate.of(2024, 1, 1));
        sat.setState(SatelliteState.ACTIVE);
        sat.setFrequencyBand("Ka-band");
        sat.setTransponderCount(24);
        sat.setEnergySystem(energy);
        sat.setConstellation(constellation);
        
        // when
        CommunicationSatellite saved = satelliteRepository.save(sat);
        
        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(satelliteRepository.findByName("Sat-001")).isPresent();
    }

    @Test
    void shouldFindByState() {
        // ... аналогично
    }
}
