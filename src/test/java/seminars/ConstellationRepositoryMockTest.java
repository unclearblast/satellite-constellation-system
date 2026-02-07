package seminars;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import seminars.domain.constellation.SatelliteConstellation;
import seminars.repository.ConstellationRepository;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Mock tests для ConstellationRepository")
class ConstellationRepositoryMockTest {

    @Mock
    private ConstellationRepository repository;

    private final String CONST_NAME = "Орбита-MockTest";

    private SatelliteConstellation constellation;

    @BeforeEach
    void setup() {
        constellation = new SatelliteConstellation(CONST_NAME);
    }

    @Test
    @DisplayName("Сохранение группировки через мок")
    void testSaveMock() {
        doNothing().when(repository).save(CONST_NAME, constellation);
        repository.save(CONST_NAME, constellation);
        verify(repository, times(1)).save(CONST_NAME, constellation);
    }

    @Test
    @DisplayName("findAll возвращает подготовленные данные")
    void testFindAllMock() {
        when(repository.findAll()).thenReturn(Map.of(CONST_NAME, constellation));

        Map<String, SatelliteConstellation> result = repository.findAll();
        assertEquals(1, result.size());
        assertEquals(constellation, result.get(CONST_NAME));
        verify(repository, times(1)).findAll();
    }
}
