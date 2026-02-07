package seminars;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import seminars.domain.constellation.SatelliteConstellation;
import seminars.repository.ConstellationRepository;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit tests для ConstellationRepository")
class ConstellationRepositoryUnitTest {

    private ConstellationRepository repository;

    private final String CONST_NAME = "Орбита-UnitTest";

    @BeforeEach
    void setup() {
        repository = new ConstellationRepository();
    }

    @Test
    @DisplayName("Сохранение группировки должно работать")
    void testSaveConstellation() {
        SatelliteConstellation constellation = new SatelliteConstellation(CONST_NAME);
        repository.save(CONST_NAME, constellation);

        assertEquals(constellation, repository.findByName(CONST_NAME));
    }

    @Test
    @DisplayName("Поиск несуществующей группировки возвращает null")
    void testFindNonExistentConstellation() {
        assertNull(repository.findByName("НеСуществует"));
    }

    @Test
    @DisplayName("findAll возвращает корректную карту")
    void testFindAll() {
        SatelliteConstellation constellation = new SatelliteConstellation(CONST_NAME);
        repository.save(CONST_NAME, constellation);

        Map<String, SatelliteConstellation> all = repository.findAll();
        assertTrue(all.containsKey(CONST_NAME));
        assertEquals(constellation, all.get(CONST_NAME));
    }
}
