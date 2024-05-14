package org.example.myproject1;

import org.example.myproject1.entity.Client;
import org.example.myproject1.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClientRepositoryIntegrationTest {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void testFindById() {
        Client client = new Client();
        client.setId(1L);
        clientRepository.save(client);

        Optional<Client> found = clientRepository.findClientById(1L);
        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
    }
}