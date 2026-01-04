package space.model;

public class CommunicationSatellite extends Satellite {

    private final double bandwidth;

    public CommunicationSatellite(String name, double batteryLevel, double bandwidth) {
        super(name, batteryLevel);
        this.bandwidth = bandwidth;
    }

    public double getBandwidth() {
        return bandwidth;
    }

    private void sendData(double amount) {
        System.out.println(name + ": Отправил " + amount + " Мбит данных!");
    }

    @Override
    protected void performMission() {
        if (!isActive) {
            System.out.println("🛑 " + name + ": Не активен");
            return;
        }

        System.out.println(name +
                ": Передача данных со скоростью " + bandwidth + " Мбит/с");
        sendData(bandwidth);
        consumeBattery(0.05);
    }

    @Override
    public String toString() {
        return "CommunicationSatellite{" +
                "bandwidth=" + bandwidth +
                ", name='" + name + '\'' +
                ", isActive=" + isActive +
                ", batteryLevel=" + batteryLevel +
                '}';
    }
}
