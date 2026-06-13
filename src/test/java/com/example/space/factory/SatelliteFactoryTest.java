package com.example.space.factory;

import com.example.space.domain.satellite.Satellite;
import com.example.space.param.CommunicationSatelliteParam;
import com.example.space.param.ImagingSatelliteParam;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SatelliteFactoryTest {

    private final CommunicationSatelliteFactory communicationFactory =
            new CommunicationSatelliteFactory();

    private final ImagingSatelliteFactory imagingFactory =
            new ImagingSatelliteFactory();

    @Test
    void shouldCreateCommunicationSatellite() {

        var param = new CommunicationSatelliteParam(
                "Comm-1",
                0.9,
                500
        );

        Satellite satellite =
                communicationFactory.createSatelliteWithParameter(param);

        assertNotNull(satellite);
        assertEquals("CommunicationSatellite", satellite.getClass().getSimpleName());
    }

    @Test
    void shouldCreateImagingSatellite() {

        var param = new ImagingSatelliteParam(
                "Img-1",
                0.8,
                2.5
        );

        Satellite satellite =
                imagingFactory.createSatelliteWithParameter(param);

        assertNotNull(satellite);
        assertEquals("ImagingSatellite", satellite.getClass().getSimpleName());
    }
}
