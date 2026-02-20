package com.example.space.factory;

import com.example.space.domain.satellite.*;

public class CommunicationSatelliteFactory implements SatelliteFactory {

    @Override
    public Satellite createSatellite(String name,
                                     EnergySystem energy,
                                     double bandwidth) {
        return new CommunicationSatellite(name, energy, bandwidth);
    }
}
