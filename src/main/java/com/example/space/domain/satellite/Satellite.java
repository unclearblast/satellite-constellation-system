package com.example.spacecenter.domain.satellite;

import com.example.spacecenter.domain.constellation.SatelliteConstellation;
import com.example.spacecenter.domain.energy.EnergySystem;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "satellite")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "satellite_type", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
public abstract class Satellite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "launch_date")
    private LocalDate launchDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SatelliteState state = SatelliteState.IDLE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "constellation_id")
    private SatelliteConstellation constellation;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "energy_system_id", referencedColumnName = "id")
    private EnergySystem energySystem;

    public void activate() {

        System.out.println("Попытка активации спутника: " + name);

        if (energy.canActivate()) {
            state.activate();
            System.out.println("✅ " + name + " успешно активирован");
        } else {
            state.deactivate("Нет энергии!");
            System.out.println("❌ " + name + " — Нет энергии!");
        }
    }

    public abstract void performMission();
}
