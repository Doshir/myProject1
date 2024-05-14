package org.example.myproject1;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.micrometer.core.instrument.MeterRegistry;
import org.example.model.GetClientInfo;
import org.example.myproject1.controller.ClientController;
import org.example.myproject1.filter.RequestThrottleFilter;
import org.example.myproject1.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ClientController.class)
@Import(RequestThrottleFilter.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;


    @MockBean
    private MeterRegistry meterRegistry;


    @Test
    public void getClientInfoTest() throws Exception {

        when(clientService.getClientInfo(anyString(), any(), anyString(), anyString()))
                .thenReturn(new GetClientInfo());



        mockMvc.perform(get("/info")
                        .header("x-platform-rquid", "baf1fa8c-4694-4251-9fb3-69e390bba983")
                        .header("x-platform-rqtm", "2024-01-01T11:12:13Z")
                        .header("x-platform-scname", "local")
                        .param("clientId", "1"))
                .andExpect(status().isOk());

        verify(clientService, times(1)).getClientInfo(anyString(), any(), anyString(), anyString());
    }

    @Test
    public void whenTooManyRequests_thenRateLimitExceeded() throws Exception {
        for (int i = 0; i < 11; i++) {
            mockMvc.perform(get("/info")
                    .header("x-platform-rquid", "baf1fa8c-4694-4251-9fb3-69e390bba983")
                    .header("x-platform-rqtm", "2024-01-01T11:12:13Z")
                    .header("x-platform-scname", "local")
                    .param("clientId", "1"));
        }

        mockMvc.perform(get("/info")
                        .header("x-platform-rquid", "baf1fa8c-4694-4251-9fb3-69e390bba983")
                        .header("x-platform-rqtm", "2024-01-01T11:12:13Z")
                        .header("x-platform-scname", "local")
                        .param("clientId", "1"))
                .andExpect(status().isTooManyRequests());
    }
}