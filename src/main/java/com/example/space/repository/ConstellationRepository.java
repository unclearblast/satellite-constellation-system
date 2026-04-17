// repository/ConstellationRepository.java
package com.example.spacecenter.repository;

import com.example.spacecenter.domain.constellation.SatelliteConstellation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ConstellationRepository extends JpaRepository<SatelliteConstellation, Long> {
    Optional<SatelliteConstellation> findByName(String name);
}
