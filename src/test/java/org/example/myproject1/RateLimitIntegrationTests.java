package org.example.myproject1;

import org.example.myproject1.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RateLimitIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    ClientRepository clientRepository;


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void whenTooManyRequests_thenRateLimitExceeded() {
        ResponseEntity<String> response = null;
        for (int i = 0; i < 11; i++) {
            response = restTemplate.getForEntity("/info?clientId=1", String.class);
        }

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void whenValidRequest_thenStatus200() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-platform-rquid", "123e4567-e89b-12d3-a456-426614174000");
        headers.set("x-platform-rqtm", Instant.now().toString());
        headers.set("x-platform-scname", "testApp");
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        ResponseEntity<String> response = restTemplate.exchange("/info?clientId=1", HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void whenRequestIsInvalid_thenStatus400() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        ResponseEntity<String> response = restTemplate.exchange("/info?clientId=1", HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void whenRequestTimesOut_thenStatus504() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-platform-rquid", "123e4567-e89b-12d3-a456-426614174000");
        headers.set("x-platform-rqtm", Instant.now().toString());
        headers.set("x-platform-scname", "testApp");
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        when(clientRepository.findClientById(1L)).thenThrow(new DataAccessResourceFailureException("Data is not allowed"));

        ResponseEntity<String> response = restTemplate.exchange("/info?clientId=1", HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.GATEWAY_TIMEOUT, response.getStatusCode());
    }
}