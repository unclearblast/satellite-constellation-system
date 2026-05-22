package com.example.tests.utils;

import io.qameta.allure.Attachment;

public class AllureUtils {
    @Attachment(value = "Request body", type = "application/json")
    public static String attachBody(String body) { return body; }
}
