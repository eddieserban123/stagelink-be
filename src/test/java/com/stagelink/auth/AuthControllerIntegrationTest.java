package com.stagelink.auth;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
        .withDatabaseName("stagelink")
        .withUsername("stagelink")
        .withPassword("stagelink");

    @LocalServerPort
    private int port;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("DB_URL", postgres::getJdbcUrl);
        registry.add("DB_USERNAME", postgres::getUsername);
        registry.add("DB_PASSWORD", postgres::getPassword);
        registry.add("FLYWAY_CLEAN_ON_START", () -> "false");
        registry.add("JWT_SECRET", () -> "integration-tests-jwt-secret-key-which-is-long-enough-1234567890");
    }

    @Test
    void shouldRegisterAndLoginPerformer() throws Exception {
        Map<String, Object> registerRequest = Map.of(
            "email", "performer.int.test@example.com",
            "password", "Password123!",
            "fullName", "Integration Performer",
            "role", "PERFORMER",
            "performerTitle", "Band",
            "performerDescription", "Integration test performer",
            "performerNumberOfMembers", 3,
            "performerSpecialRequirements", "2 microphones"
        );

        HttpRequest registerHttpRequest = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl() + "/api/auth/register"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(registerRequest)))
            .build();
        HttpResponse<String> registerResponse = httpClient.send(
            registerHttpRequest,
            HttpResponse.BodyHandlers.ofString()
        );
        Map<String, Object> registerResponseBody = objectMapper.readValue(registerResponse.body(), new TypeReference<>() {});

        assertThat(registerResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(registerResponseBody).isNotNull();
        assertThat(registerResponseBody.get("accessToken")).isNotNull();
        assertThat(registerResponseBody.get("email")).isEqualTo("performer.int.test@example.com");

        Map<String, Object> loginRequest = Map.of(
            "email", "performer.int.test@example.com",
            "password", "Password123!",
            "role", "PERFORMER"
        );

        HttpRequest loginHttpRequest = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl() + "/api/auth/login"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(loginRequest)))
            .build();
        HttpResponse<String> loginResponse = httpClient.send(
            loginHttpRequest,
            HttpResponse.BodyHandlers.ofString()
        );
        Map<String, Object> loginResponseBody = objectMapper.readValue(loginResponse.body(), new TypeReference<>() {});

        assertThat(loginResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(loginResponseBody).isNotNull();
        assertThat(loginResponseBody.get("accessToken")).isNotNull();
        assertThat(loginResponseBody.get("email")).isEqualTo("performer.int.test@example.com");
    }

    private String baseUrl() {
        return "http://localhost:" + port;
    }
}
