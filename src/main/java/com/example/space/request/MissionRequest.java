package com.example.space.request;

public class MissionRequest {

    private final String constellationName;

    public MissionRequest(String constellationName) {
        this.constellationName = constellationName;
    }

    public String getConstellationName() {
        return constellationName;
    }
}
