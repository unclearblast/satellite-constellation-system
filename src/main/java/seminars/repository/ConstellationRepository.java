package seminars.repository;

import org.springframework.stereotype.Repository;
import seminars.domain.constellation.SatelliteConstellation;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ConstellationRepository {

    private final Map<String, SatelliteConstellation> constellations = new HashMap<>();

    public void save(String name, SatelliteConstellation constellation) {
        constellations.put(name, constellation);
    }

    public SatelliteConstellation findByName(String name) {
        return constellations.get(name);
    }

    public Map<String, SatelliteConstellation> findAll() {
        return constellations;
    }
}
