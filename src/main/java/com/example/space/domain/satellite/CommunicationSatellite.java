package com.example.spacecenter.domain.satellite;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("COMMUNICATION")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CommunicationSatellite extends Satellite {

    @Column(name = "frequency_band")
    private String frequencyBand; // например, "Ka-band", "Ku-band"

    @Column(name = "transponder_count")
    private Integer transponderCount;
    @Override
    public void performMission() {

        if (!state.isActive()) {
            System.out.println("🛑 " + name + ": не активен");
            return;
        }

        System.out.println(
                "📡 " + name + ": Передача данных со скоростью "
                        + bandwidth + " Мбит/с"
        );

        energy.consume(0.05);
    }

    @Override
    public String toString() {
        return "CommunicationSatellite{" +
                "name='" + name + '\'' +
                ", bandwidth=" + bandwidth +
                ", state=" + state +
                ", energy=" + energy +
                '}';
    }
}
