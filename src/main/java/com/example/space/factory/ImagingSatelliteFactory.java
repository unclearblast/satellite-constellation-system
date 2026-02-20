package com.example.space.factory;

import com.example.space.domain.satellite.*;
import com.example.space.exception.SpaceOperationException;
import com.example.space.param.*;

import org.springframework.stereotype.Component;

@Component
public class ImagingSatelliteFactory implements SatelliteFactory {

    @Override
    public Satellite createSatelliteWithParameter(SatelliteParam param) {

        if (!(param instanceof ImagingSatelliteParam imagingParam)) {
            throw new SpaceOperationException("Неверный тип параметра для спутника ДЗЗ");
        }

        EnergySystem energy = EnergySystem.builder()
                .batteryLevel(imagingParam.getBatteryLevel())
                .build();

        return new ImagingSatellite(
                imagingParam.getName(),
                energy,
                imagingParam.getResolution()
        );
    }

    @Override
    public boolean isSatelliteTypeSupported(SatelliteType type) {
        return type == SatelliteType.IMAGE;
    }
}
