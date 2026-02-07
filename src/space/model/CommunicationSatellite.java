package space.model;

public class CommunicationSatellite extends Satellite {

    private final double bandwidth;

    public CommunicationSatellite(String name, double initialBattery, double bandwidth) {
        super(name, initialBattery);
        this.bandwidth = bandwidth;
    }

    private void sendData(double amount) {
        System.out.println(name + ": Отправил " + amount + " Мбит данных!");
    }

    @Override
    protected void performMission() {
        if (!state.isActive()) {
            System.out.println("🛑 " + name + ": Не активен");
            return;
        }

        System.out.println(name + ": Передача данных со скоростью " + bandwidth + " Мбит/с");
        sendData(bandwidth);
        energy.consume(0.05);
        if (!energy.canActivate()) state.deactivate();
    }

    @Override
    public String toString() {
        return "CommunicationSatellite{" +
                "bandwidth=" + bandwidth +
                ", name='" + name + '\'' +
                ", isActive=" + state.isActive() +
                ", batteryLevel=" + energy.getBatteryLevel() +
                '}';
    }
}
