package com.example.space.service;

import com.example.space.domain.satellite.Satellite;
import com.example.space.param.ImagingSatelliteParam;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SatelliteServiceTest {

    @Autowired
    private SatelliteService satelliteService;

    @Test
    void shouldCreateSatelliteViaService() {

        var param = new ImagingSatelliteParam(
                "Service-Img",
                0.95,
                1.2
        );

        Satellite satellite = satelliteService.createSatellite(param);

        assertNotNull(satellite);
        assertEquals("ImagingSatellite", satellite.getClass().getSimpleName());
    }
}
