package com.example.tests.tests;

import com.example.tests.config.TestConfig;
import com.example.tests.models.EnergySystem;
import io.qameta.allure.*;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Epic("Energy System Management")
@Feature("Operations on Energy Systems")
public class EnergySystemTests extends TestConfig {

    private static Long createdEnergySystemId;
    private static Long existingSatelliteId = 1L; // предположим, что спутник с ID=1 существует

    @Test
    @Story("GET all energy systems")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("GET /api/energy-systems - should return list")
    void testGetAllEnergySystems() {
        List<EnergySystem> systems = given()
                .when()
                .get("/api/energy-systems")
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<>() {});

        assertThat(systems, is(notNullValue()));
    }

    @Test
    @Story("POST create energy system")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("POST /api/energy-systems - create new energy system for a satellite")
    void testCreateEnergySystem() {
        EnergySystem newSystem = new EnergySystem();
        newSystem.setSatelliteId(existingSatelliteId);
        newSystem.setBatteryLevel(85.5);
        newSystem.setSolarPanelEfficiency(92.0);

        EnergySystem created = given()
                .body(newSystem)
                .when()
                .post("/api/energy-systems")
                .then()
                .statusCode(201)
                .extract()
                .as(EnergySystem.class);

        assertThat(created.getId(), is(notNullValue()));
        assertThat(created.getSatelliteId(), is(existingSatelliteId));
        createdEnergySystemId = created.getId();
    }

    @Test
    @Story("GET energy system by ID")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("GET /api/energy-systems/{id} - get single energy system")
    void testGetEnergySystemById() {
        if (createdEnergySystemId == null) testCreateEnergySystem();

        given()
                .pathParam("id", createdEnergySystemId)
                .when()
                .get("/api/energy-systems/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(createdEnergySystemId.intValue()))
                .body("satelliteId", equalTo(existingSatelliteId.intValue()));
    }

    @Test
    @Story("PUT update energy system")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("PUT /api/energy-systems/{id} - update energy system")
    void testUpdateEnergySystem() {
        if (createdEnergySystemId == null) testCreateEnergySystem();

        EnergySystem updatePayload = new EnergySystem();
        updatePayload.setBatteryLevel(95.0);
        updatePayload.setSolarPanelEfficiency(88.5);

        EnergySystem updated = given()
                .pathParam("id", createdEnergySystemId)
                .body(updatePayload)
                .when()
                .put("/api/energy-systems/{id}")
                .then()
                .statusCode(200)
                .extract()
                .as(EnergySystem.class);

        assertThat(updated.getBatteryLevel(), is(95.0));
        assertThat(updated.getSolarPanelEfficiency(), is(88.5));
    }

    @Test
    @Story("DELETE energy system")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("DELETE /api/energy-systems/{id} - delete energy system")
    void testDeleteEnergySystem() {
        if (createdEnergySystemId == null) testCreateEnergySystem();

        given()
                .pathParam("id", createdEnergySystemId)
                .when()
                .delete("/api/energy-systems/{id}")
                .then()
                .statusCode(204);

        // Verify deletion
        given()
                .pathParam("id", createdEnergySystemId)
                .when()
                .get("/api/energy-systems/{id}")
                .then()
                .statusCode(404);
    }
}
