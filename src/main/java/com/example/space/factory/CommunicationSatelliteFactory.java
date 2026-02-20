package com.example.space.factory;

import com.example.space.domain.satellite.*;
import com.example.space.exception.SpaceOperationException;
import com.example.space.param.*;

import org.springframework.stereotype.Component;

@Component
public class CommunicationSatelliteFactory implements SatelliteFactory {

    @Override
    public Satellite createSatelliteWithParameter(SatelliteParam param) {

        if (!(param instanceof CommunicationSatelliteParam communicationParam)) {
            throw new SpaceOperationException("Неверный тип параметра для спутника связи");
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
