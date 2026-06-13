package com.example.space.request;

import com.example.space.param.SatelliteParam;

public class AddSatelliteRequest {

    private final String constellationName;
    private final SatelliteParam satelliteParam;

    public AddSatelliteRequest(String constellationName,
                               SatelliteParam satelliteParam) {
        this.constellationName = constellationName;
        this.satelliteParam = satelliteParam;
    }

    public String getConstellationName() {
        return constellationName;
    }

    public SatelliteParam getSatelliteParam() {
        return satelliteParam;
    }
}
