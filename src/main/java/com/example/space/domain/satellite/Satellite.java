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
