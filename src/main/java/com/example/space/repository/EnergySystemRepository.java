// repository/EnergySystemRepository.java
package com.example.spacecenter.repository;

import com.example.spacecenter.domain.energy.EnergySystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnergySystemRepository extends JpaRepository<EnergySystem, Long> {
}
