package com.example.space.service;

import com.example.space.domain.satellite.Satellite;
import com.example.space.param.SatelliteParam;

public interface SatelliteService {

    Satellite createSatellite(SatelliteParam param);
}