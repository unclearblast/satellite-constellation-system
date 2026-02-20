package com.example.space.factory;

import com.example.space.domain.satellite.EnergySystem;
import com.example.space.domain.satellite.Satellite;

public interface SatelliteFactory {

    Satellite createSatellite(String name,
                              EnergySystem energy,
                              double parameter);
}
