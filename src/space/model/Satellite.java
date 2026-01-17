package space.model;

public abstract class Satellite {

    protected final String name;
    protected final EnergySystem energy;
    protected final SatelliteState state;

    protected Satellite(String name, double initialBattery) {
        this.name = name;
        this.energy = new EnergySystem(initialBattery);
        this.state = new SatelliteState();
    }

    public boolean activate() {
        if (energy.canActivate()) {
            state.activate();
            return true;
        }
        return false;
    }

    public void deactivate() {
        state.deactivate();
    }

    protected abstract void performMission();

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return state.isActive();
    }

    public double getBatteryLevel() {
        return energy.getBatteryLevel();
    }

    @Override
    public abstract String toString();
}

