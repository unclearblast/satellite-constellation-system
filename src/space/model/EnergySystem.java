package space.model;

public class EnergySystem {

    private double batteryLevel;
    private final double MIN_BATTERY = 0.2;

    public EnergySystem(double initialBattery) {
        this.batteryLevel = initialBattery;
    }

    public boolean canActivate() {
        return batteryLevel > MIN_BATTERY;
    }

    public void consume(double amount) {
        batteryLevel -= amount;
        if (batteryLevel < 0) batteryLevel = 0;
    }

    public double getBatteryLevel() {
        return batteryLevel;
    }
}
