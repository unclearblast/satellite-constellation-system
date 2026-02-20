package com.example.space.domain.satellite;

public abstract class Satellite {

    protected final String name;
    protected final EnergySystem energy;
    protected final SatelliteState state;

    protected Satellite(String name, EnergySystem energy) {
        this.name = name;
        this.energy = energy;
        this.state = new SatelliteState();
    }

    public void activate() {
        if (energy.canActivate()) {
            state.activate();
        } else {
            state.deactivate("Недостаточно энергии");
        }
    }

    public abstract void performMission();
}
