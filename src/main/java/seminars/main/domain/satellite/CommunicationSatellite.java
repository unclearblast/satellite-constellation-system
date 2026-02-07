package seminars.domain.satellite;

public class CommunicationSatellite extends Satellite {

    private final double bandwidth;

    public CommunicationSatellite(
            String name,
            double bandwidth,
            EnergySystem energy,
            SatelliteState state
    ) {
        super(name, energy, state);
        this.bandwidth = bandwidth;
    }

    @Override
    public void performMission() {
        if (!state.isActive()) {
            System.out.println("🛑 " + name + ": не активен");
            return;
        }

        System.out.println(
                name + ": Передача данных со скоростью " + bandwidth + " Мбит/с"
        );
        energy.consume(0.05);
    }

    @Override
    public String toString() {
        return "CommunicationSatellite{" +
                "name='" + name + '\'' +
                ", bandwidth=" + bandwidth +
                ", state=" + state +
                ", energy=" + energy +
                '}';
    }
}
