package com.example.tests.config;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeAll;

public class TestConfig {
    @BeforeAll
    public static void setup() {
        // Порт основного приложения (измените при необходимости)
        RestAssured.port = 8080;
        RestAssured.baseURI = "http://localhost";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }
}
