package com.example.tests.tests;

import com.example.tests.config.TestConfig;
import com.example.tests.endpoints.SatelliteEndpoints;
import com.example.tests.models.Satellite;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Epic("API тестирование спутников")
@Feature("CRUD операции")
public class SatelliteTests extends TestConfig {

    private String createdSatelliteId;

    @Test
    @Story("Создание спутника")
    @DisplayName("Позитивный тест: POST /satellites -> 201 Created")
    public void testCreateSatellite() {
        Satellite newSat = new Satellite("TestSat-1", "communication", "ACTIVE");
        var response = SatelliteEndpoints.createSatellite(newSat);
        response.then()
                .statusCode(201)
                .body("name", equalTo("TestSat-1"))
                .body("type", equalTo("communication"));
        createdSatelliteId = response.jsonPath().getString("id");
        Allure.addAttachment("Created ID", createdSatelliteId);
    }

    @Test
    @Story("Получение всех спутников")
    @DisplayName("Позитивный тест: GET /satellites -> 200 OK")
    public void testGetAllSatellites() {
        SatelliteEndpoints.getAllSatellites()
                .then()
                .statusCode(200)
                .body("$", isA(java.util.List.class));
    }

    @Test
    @Story("Получение спутника по ID")
    @DisplayName("Позитивный тест: GET /satellites/{id} -> 200 OK")
    public void testGetSatelliteById() {
        SatelliteEndpoints.getSatelliteById(createdSatelliteId)
                .then()
                .statusCode(200)
                .body("id", equalTo(createdSatelliteId));
    }

    @Test
    @Story("Обновление спутника")
    @DisplayName("Позитивный тест: PUT /satellites/{id} -> 200 OK")
    public void testUpdateSatellite() {
        Satellite updated = new Satellite("UpdatedName", "imaging", "MAINTENANCE");
        SatelliteEndpoints.updateSatellite(createdSatelliteId, updated)
                .then()
                .statusCode(200)
                .body("name", equalTo("UpdatedName"));
    }

    @Test
    @Story("Удаление спутника")
    @DisplayName("Позитивный тест: DELETE /satellites/{id} -> 204 No Content")
    public void testDeleteSatellite() {
        SatelliteEndpoints.deleteSatellite(createdSatelliteId)
                .then()
                .statusCode(204);
        // Проверяем, что действительно удалён
        SatelliteEndpoints.getSatelliteById(createdSatelliteId)
                .then()
                .statusCode(404);
    }
}
