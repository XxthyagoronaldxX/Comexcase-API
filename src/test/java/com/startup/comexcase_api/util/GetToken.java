package com.startup.comexcase_api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import java.util.HashMap;
import java.util.Map;

public class GetToken {
    public static String execute(String username, String password, int port) {
        Map<String, Object> loginDTO = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        loginDTO.put("username", username);
        loginDTO.put("password", password);

        String loginDTOBody = null;
        try {
            loginDTOBody = objectMapper.writeValueAsString(loginDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return RestAssured
                .given()
                .basePath("/login")
                .port(port)
                .contentType(ContentType.JSON)
                .body(loginDTOBody)
                .post()
                .getBody()
                .asString();
    }
}
