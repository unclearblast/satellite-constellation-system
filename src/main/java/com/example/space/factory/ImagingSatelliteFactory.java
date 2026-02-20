package com.example.space.factory;

import com.example.space.domain.satellite.*;

public class ImagingSatelliteFactory implements SatelliteFactory {

    @Override
    public Satellite createSatellite(String name,
                                     EnergySystem energy,
                                     double resolution) {
        return new ImagingSatellite(name, energy, resolution);
    }
}
