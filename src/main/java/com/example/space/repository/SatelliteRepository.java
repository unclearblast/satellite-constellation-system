// repository/SatelliteRepository.java
package com.example.spacecenter.repository;

import com.example.spacecenter.domain.satellite.Satellite;
import com.example.spacecenter.domain.satellite.SatelliteState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SatelliteRepository extends JpaRepository<Satellite, Long> {
    Optional<Satellite> findByName(String name);
    List<Satellite> findByState(SatelliteState state);
    List<Satellite> findByConstellationId(Long constellationId);
}
