package com.example.spacecenter.domain.constellation;

import com.example.spacecenter.domain.satellite.Satellite;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "satellite_constellation")
@Data
@NoArgsConstructor
public class SatelliteConstellation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    private String description;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @OneToMany(mappedBy = "constellation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Satellite> satellites = new ArrayList<>();

    public void addSatellite(Satellite satellite) {
        satellites.add(satellite);
        satellite.setConstellation(this);
    }

    public void removeSatellite(Satellite satellite) {
        satellites.remove(satellite);
        satellite.setConstellation(null);
    }
}
