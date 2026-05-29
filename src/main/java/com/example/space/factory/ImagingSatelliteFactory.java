package com.example.space.factory;

import com.example.space.domain.satellite.EnergySystem;
import com.example.space.domain.satellite.ImagingSatellite;
import com.example.space.domain.satellite.Satellite;
import com.example.space.exception.SpaceOperationException;
import com.example.space.param.ImagingSatelliteParam;
import com.example.space.param.SatelliteParam;
import com.example.space.param.SatelliteType;

public class ImagingSatelliteFactory implements SatelliteFactory {

    @Override
    public Satellite createSatelliteWithParameter(SatelliteParam param) {

        if (!(param instanceof ImagingSatelliteParam imagingParam)) {
            throw new SpaceOperationException(
                    "Неверный параметр для ImagingSatellite"
            );
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
