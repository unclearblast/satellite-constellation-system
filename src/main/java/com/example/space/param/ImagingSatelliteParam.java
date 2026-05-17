package com.example.space.param;

public class ImagingSatelliteParam extends SatelliteParam {

    private final double resolution;

    public ImagingSatelliteParam(String name,
                                 double batteryLevel,
                                 double resolution) {
        super(SatelliteType.IMAGE, name, batteryLevel);
        this.resolution = resolution;
    }

    public double getResolution() {
        return resolution;
    }
}
