package org.example.myproject1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.example.model.Payload;
import org.example.myproject1.entity.Client;
import org.example.myproject1.model.LogModel;
import org.example.myproject1.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

@SpringBootTest
public class DataMaskingTests {

    @Autowired
    private ClientService clientService;

    @Test
    public void testDataMasking() {
        Client client = new Client();
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setMiddleName("Franklin");
        Instant birthday = Instant.parse("2022-01-01T00:00:00Z");
        client.setBirthday(birthday);
        client.setBirthPlace("York");

        LogModel maskedPayload = clientService.maskClientDataLog(client);

        assertEquals("J***", maskedPayload.getFirstName());
        assertEquals("D**", maskedPayload.getLastName());
        assertEquals("F*******", maskedPayload.getMiddleName());
        assertEquals("2*******************",maskedPayload.getBirthday());
        assertEquals("Y***", maskedPayload.getBirthPlace());

    }
}