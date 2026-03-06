package com.example.space.factory;

import com.example.space.domain.satellite.CommunicationSatellite;
import com.example.space.domain.satellite.EnergySystem;
import com.example.space.domain.satellite.Satellite;
import com.example.space.exception.SpaceOperationException;
import com.example.space.param.CommunicationSatelliteParam;
import com.example.space.param.SatelliteParam;
import com.example.space.param.SatelliteType;

public class CommunicationSatelliteFactory implements SatelliteFactory {

    @Override
    public Satellite createSatelliteWithParameter(SatelliteParam param) {

        if (!(param instanceof CommunicationSatelliteParam communicationParam)) {
            throw new SpaceOperationException(
                    "Неверный параметр для CommunicationSatellite"
            );
        }

        EnergySystem energy = EnergySystem.builder()
                .batteryLevel(communicationParam.getBatteryLevel())
                .build();

        return new CommunicationSatellite(
                communicationParam.getName(),
                energy,
                communicationParam.getBandwidth()
        );
    }

    @Override
    public boolean isSatelliteTypeSupported(SatelliteType type) {
        return type == SatelliteType.COMMUNICATION;
    }
}
