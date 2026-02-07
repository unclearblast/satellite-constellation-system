package seminars.service;

import org.springframework.stereotype.Service;
import seminars.domain.constellation.SatelliteConstellation;
import seminars.domain.satellite.Satellite;
import seminars.repository.ConstellationRepository;

import java.util.Map;

@Service
public class SpaceOperationCenterService {

    private final ConstellationRepository repository;

    public SpaceOperationCenterService(ConstellationRepository repository) {
        this.repository = repository;
    }

    public void createAndSaveConstellation(String name) {
        SatelliteConstellation constellation = new SatelliteConstellation(name);
        repository.save(name, constellation);
        System.out.println("Создана и сохранена группировка: " + name);
    }

    public void addSatelliteToConstellation(String name, Satellite satellite) {
        repository.findByName(name).addSatellite(satellite);
    }

    public void activateAllSatellites(String name) {
        repository.findByName(name).activateAll();
    }

    public void executeConstellationMission(String name) {
        repository.findByName(name).executeMissions();
    }

    public void showConstellationStatus(String name) {
        System.out.println(repository.findByName(name));
    }

    public Map<String, SatelliteConstellation> getAllConstellations() {
        return repository.findAll();
    }
}
