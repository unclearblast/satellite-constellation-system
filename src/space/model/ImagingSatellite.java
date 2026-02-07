package space.model;

public class ImagingSatellite extends Satellite {

    private final double resolution;
    private int photosTaken;

    public ImagingSatellite(String name, double initialBattery, double resolution) {
        super(name, initialBattery);
        this.resolution = resolution;
        this.photosTaken = 0;
    }

    private void takePhoto() {
        photosTaken++;
        System.out.println(name + ": Снимок #" + photosTaken + " сделан!");
    }

    @Override
    protected void performMission() {
        if (!state.isActive()) {
            System.out.println("🛑 " + name + ": Не может выполнить съемку - не активен");
            return;
        }

        System.out.println(name + ": Съемка территории с разрешением " + resolution + " м/пиксель");
        takePhoto();
        energy.consume(0.08);
        if (!energy.canActivate()) state.deactivate();
    }

    @Override
    public String toString() {
        return "ImagingSatellite{" +
                "resolution=" + resolution +
                ", photosTaken=" + photosTaken +
                ", name='" + name + '\'' +
                ", isActive=" + state.isActive() +
                ", batteryLevel=" + energy.getBatteryLevel() +
                '}';
    }
}
