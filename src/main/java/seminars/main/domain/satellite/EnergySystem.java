package seminars.domain.satellite;

public class EnergySystem {

    private double batteryLevel;

    public EnergySystem(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public boolean canActivate() {
        return batteryLevel > 0.2;
    }

    public void consume(double amount) {
        batteryLevel = Math.max(0, batteryLevel - amount);
    }

    public double getBatteryLevel() {
        return batteryLevel;
    }

    @Override
    public String toString() {
        return "EnergySystem{batteryLevel=" + batteryLevel + '}';
    }
}
