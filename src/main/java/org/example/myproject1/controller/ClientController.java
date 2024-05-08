package org.example.myproject1.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.example.api.InfoApi;
import org.example.model.GetClientInfo;
import org.example.myproject1.service.ClientService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;

@RestController
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientController implements InfoApi {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    public ResponseEntity<GetClientInfo> getClientInfo(
            @NotNull @Pattern(regexp = "^([0-9A-Za-z-]{36})$") @Parameter(name = "x-platform-rquid", description = "Уникальный идентификатор запроса", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "x-platform-rquid", required = true) String xPlatformRquid,
            @NotNull @Pattern(regexp = "^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z$") @Parameter(name = "x-platform-rqtm", description = "Дата и время запроса", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "x-platform-rqtm", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime xPlatformRqtm,
            @NotNull @Pattern(regexp = "^[0-9A-Za-z]{0,10}$") @Parameter(name = "x-platform-scname", description = "Идентификатор системы отправителя запроса", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "x-platform-scname", required = true) String xPlatformScname,
            @NotNull @Pattern(regexp = "^[0-9]{0,50}$") @Size(max = 50) @Parameter(name = "clientId", description = "Идентификатор клиента", required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "clientId", required = true) String clientId
    )
    {
        GetClientInfo clientInfo = clientService.getClientInfo(xPlatformRquid, xPlatformRqtm, xPlatformScname, clientId);
        return new ResponseEntity<>(clientInfo, HttpStatus.OK);
    }
}
