package com.example.tests.endpoints;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class SatelliteEndpoints {
    private static final String BASE_PATH = "/satellites";

    public static Response createSatellite(Object satellite) {
        return given()
                .contentType(ContentType.JSON)
                .body(satellite)
                .post(BASE_PATH);
    }

    public static Response getAllSatellites() {
        return given().get(BASE_PATH);
    }

    public static Response getSatelliteById(String id) {
        return given().get(BASE_PATH + "/" + id);
    }

    public static Response updateSatellite(String id, Object satellite) {
        return given()
                .contentType(ContentType.JSON)
                .body(satellite)
                .put(BASE_PATH + "/" + id);
    }

    public static Response deleteSatellite(String id) {
        return given().delete(BASE_PATH + "/" + id);
    }
}
