package com.example.spacecenter.domain.satellite;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("IMAGING")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ImagingSatellite extends Satellite {

    @Column(name = "resolution_meters")
    private Double resolutionMeters; // метры на пиксель

    @Column(name = "sensor_type")
    private String sensorType; // "Optical", "SAR", "Infrared"
    @Override
    public void performMission() {

        if (!state.isActive()) {
            System.out.println("🛑 " + name + ": не активен");
            return;
        }

        System.out.println(
                "📷 " + name + ": Съемка территории ("
                        + resolution + " м/пиксель)"
        );

        photosTaken++;
        energy.consume(0.08);
    }

    @Override
    public String toString() {
        return "ImagingSatellite{" +
                "name='" + name + '\'' +
                ", resolution=" + resolution +
                ", photosTaken=" + photosTaken +
                ", state=" + state +
                ", energy=" + energy +
                '}';
    }
}
