package seminars.domain.constellation;

import seminars.domain.satellite.Satellite;
import java.util.ArrayList;
import java.util.List;

public class SatelliteConstellation {

    private final String constellationName;
    private final List<Satellite> satellites = new ArrayList<>();

    public SatelliteConstellation(String constellationName) {
        this.constellationName = constellationName;
    }

    public void addSatellite(Satellite satellite) {
        satellites.add(satellite);
        System.out.println(
                satellite + " добавлен в группировку '" + constellationName + "'"
        );
    }

    public void activateAll() {
        satellites.forEach(Satellite::activate);
    }

    public void executeMissions() {
        satellites.forEach(Satellite::performMission);
    }

    public List<Satellite> getSatellites() {
        return satellites;
    }

    @Override
    public String toString() {
        return "SatelliteConstellation{" +
                "constellationName='" + constellationName + '\'' +
                ", satellites=" + satellites +
                '}';
    }
}
