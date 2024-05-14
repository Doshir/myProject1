package org.example.myproject1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RateLimitIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void whenTooManyRequests_thenRateLimitExceeded() {
        ResponseEntity<String> response = null;
        for (int i = 0; i < 11; i++) {
            response = restTemplate.getForEntity("/info?clientId=1", String.class);
        }

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
    }
    
}