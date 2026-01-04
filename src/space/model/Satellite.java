package space.model;

public abstract class Satellite {

    protected final String name;
    protected boolean isActive;
    protected double batteryLevel;

    protected static final double MIN_BATTERY_TO_ACTIVATE = 0.2;

    protected Satellite(String name, double batteryLevel) {
        this.name = name;
        this.batteryLevel = batteryLevel;
        this.isActive = false;
    }

    public boolean activate() {
        if (batteryLevel > MIN_BATTERY_TO_ACTIVATE) {
            isActive = true;
            return true;
        }
        return false;
    }

    public void deactivate() {
        isActive = false;
    }

    protected void consumeBattery(double amount) {
        batteryLevel -= amount;
        if (batteryLevel <= MIN_BATTERY_TO_ACTIVATE) {
            batteryLevel = Math.max(batteryLevel, 0);
            deactivate();
        }
    }

    protected abstract void performMission();

    public String getName() {
        return name;
    }

    public double getBatteryLevel() {
        return batteryLevel;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public abstract String toString();
}
