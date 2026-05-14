package com.example.spacecenter.domain.energy;

import com.example.spacecenter.domain.satellite.Satellite;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "energy_system")
@Data
@NoArgsConstructor
public class EnergySystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "power_capacity_watts", nullable = false)
    private Double powerCapacityWatts;

    @Column(name = "current_power_watts", nullable = false)
    private Double currentPowerWatts;

    @Column(name = "battery_capacity_watt_hours")
    private Double batteryCapacityWattHours;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnergySystemStatus status = EnergySystemStatus.NOMINAL;

    // Обратная связь для удобства, но владельцем является Satellite
    @OneToOne(mappedBy = "energySystem")
    private Satellite satellite;

    public enum EnergySystemStatus {
        NOMINAL, LOW_POWER, CRITICAL, CHARGING
    }
}
