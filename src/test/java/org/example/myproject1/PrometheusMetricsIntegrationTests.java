package org.example.myproject1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PrometheusMetricsIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void whenAccessingPrometheusEndpoint_thenStatus200() {
        ResponseEntity<String> response = restTemplate.getForEntity("/actuator/prometheus", String.class);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void whenAccessingPrometheusEndpoint_thenMetricsArePresent() {
        ResponseEntity<String> response = restTemplate.getForEntity("/actuator/prometheus", String.class);
        String responseBody = response.getBody();

        // Check for some expected metrics in the response body
        boolean containsHttpRequestDuration = responseBody.contains("http_request_duration_seconds");
        boolean containsResponses = responseBody.contains("responses_total");

        assertEquals(true, containsHttpRequestDuration);
        assertEquals(true, containsResponses);
    }
}