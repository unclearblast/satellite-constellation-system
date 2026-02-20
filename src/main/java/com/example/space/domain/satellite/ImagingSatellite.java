package com.example.space.domain.satellite;

public class ImagingSatellite extends Satellite {

    private final double resolution;
    private int photosTaken;

    public ImagingSatellite(
            String name,
            EnergySystem energy,
            double resolution
    ) {
        super(name, energy);
        this.resolution = resolution;
    }

    @Override
    public void performMission() {
        if (!state.isActive()) {
            System.out.println("🛑 " + name + ": не активен");
            return;
        }

        System.out.println(
                name + ": Съемка территории (" + resolution + " м/пиксель)"
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
