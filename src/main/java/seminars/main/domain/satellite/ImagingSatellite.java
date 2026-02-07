package seminars.domain.satellite;

public class ImagingSatellite extends Satellite {

    private final double resolution;
    private int photosTaken;

    public ImagingSatellite(
            String name,
            double resolution,
            EnergySystem energy,
            SatelliteState state
    ) {
        super(name, energy, state);
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
