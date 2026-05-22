package com.example.tests.tests;

import com.example.tests.config.TestConfig;
import com.example.tests.models.Constellation;
import io.qameta.allure.*;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Epic("Constellation Management")
@Feature("CRUD Operations on Constellations")
public class ConstellationTests extends TestConfig {

    private static Long createdConstellationId;

    @Test
    @Story("GET all constellations")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("GET /api/constellations - should return list")
    void testGetAllConstellations() {
        List<Constellation> constellations = given()
                .when()
                .get("/api/constellations")
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<>() {});

        assertThat(constellations, is(notNullValue()));
    }

    @Test
    @Story("POST create constellation")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("POST /api/constellations - create new constellation")
    void testCreateConstellation() {
        Constellation newConst = new Constellation();
        newConst.setName("AutoTestConst");
        newConst.setDescription("Created by automation");

        Constellation created = given()
                .body(newConst)
                .when()
                .post("/api/constellations")
                .then()
                .statusCode(201)
                .extract()
                .as(Constellation.class);

        assertThat(created.getId(), is(notNullValue()));
        assertThat(created.getName(), is("AutoTestConst"));
        createdConstellationId = created.getId();
    }

    @Test
    @Story("GET constellation by ID")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("GET /api/constellations/{id} - get single constellation")
    void testGetConstellationById() {
        if (createdConstellationId == null) testCreateConstellation();

        given()
                .pathParam("id", createdConstellationId)
                .when()
                .get("/api/constellations/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(createdConstellationId.intValue()))
                .body("name", equalTo("AutoTestConst"));
    }

    @Test
    @Story("PUT update constellation")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("PUT /api/constellations/{id} - update constellation")
    void testUpdateConstellation() {
        if (createdConstellationId == null) testCreateConstellation();

        Constellation updatePayload = new Constellation();
        updatePayload.setDescription("Updated description");

        Constellation updated = given()
                .pathParam("id", createdConstellationId)
                .body(updatePayload)
                .when()
                .put("/api/constellations/{id}")
                .then()
                .statusCode(200)
                .extract()
                .as(Constellation.class);

        assertThat(updated.getDescription(), is("Updated description"));
    }

    @Test
    @Story("DELETE constellation")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("DELETE /api/constellations/{id} - delete constellation")
    void testDeleteConstellation() {
        if (createdConstellationId == null) testCreateConstellation();

        given()
                .pathParam("id", createdConstellationId)
                .when()
                .delete("/api/constellations/{id}")
                .then()
                .statusCode(204);

        // Verify deletion
        given()
                .pathParam("id", createdConstellationId)
                .when()
                .get("/api/constellations/{id}")
                .then()
                .statusCode(404);
    }
}
