package com.example.space.factory;

import com.example.space.domain.satellite.Satellite;
import com.example.space.param.SatelliteParam;
import com.example.space.param.SatelliteType;

public interface SatelliteFactory {

    Satellite createSatelliteWithParameter(SatelliteParam param);

    boolean isSatelliteTypeSupported(SatelliteType type);
}
