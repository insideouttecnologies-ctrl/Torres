package com.us.Torres;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.us.Torres.models.Paciente;
import com.us.Torres.repository.PacienteRepository;
import com.us.Torres.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @LocalServerPort
    private int port;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void defaultAdminUserShouldBeSeeded() {
        assertThat(userRepository.findByEmail("admin@hospital.com")).isPresent();
    }

    @Test
    void loginEndpointShouldAuthenticateTheDefaultAdmin() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/api/v1/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"email\":\"admin@hospital.com\",\"password\":\"SenhaForte@2026\"}"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(200);
        Map<String, Object> body = objectMapper.readValue(response.body(), Map.class);
        assertThat(body.get("token")).isNotNull();
        assertThat(body.get("mensagem")).isEqualTo("Autenticação realizada com sucesso");
    }

    @Test
    void patientEndpointShouldCreateANewPatient() throws IOException, InterruptedException {
        pacienteRepository.deleteAll();

        HttpRequest loginRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/api/v1/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"email\":\"admin@hospital.com\",\"password\":\"SenhaForte@2026\"}"))
                .build();

        HttpResponse<String> loginResponse = httpClient.send(loginRequest, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> loginBody = objectMapper.readValue(loginResponse.body(), Map.class);
        String token = (String) loginBody.get("token");

        assertThat(token).isNotBlank();

        Paciente paciente = Paciente.builder()
                .nome("Maria Costa")
                .bi("987654321LA")
                .genero("Feminino")
                .telefone("+244923222333")
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/api/v1/paciente"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(paciente)))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(201);
        Map<String, Object> body = objectMapper.readValue(response.body(), Map.class);
        assertThat(body.get("nome")).isEqualTo("Maria Costa");
        assertThat(body.get("bi")).isEqualTo("987654321LA");
    }
}
