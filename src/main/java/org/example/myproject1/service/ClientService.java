package org.example.myproject1.service;

import jakarta.validation.ConstraintViolationException;
import org.example.model.GetClientInfo;
import org.example.model.Payload;
import org.example.model.Status;
import org.example.myproject1.entity.Client;
import org.example.myproject1.model.LogModel;
import org.example.myproject1.repository.ClientRepository;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final Logger log = LoggerFactory.getLogger(ClientService.class);

    private static final String STATUS_SUCCESS_CODE = "0";
    private static final String STATUS_SUCCESS_NAME = "Success";
    private static final String STATUS_SUCCESS_DESCRIPTION = "Successful response from server";

    private static final String STATUS_BAD_REQUEST_CODE = "1";
    private static final String STATUS_BAD_REQUEST_NAME = "Not valid request";
    private static final String STATUS_BAD_REQUEST_DESCRIPTION = "Required field is missing";


    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public GetClientInfo getClientInfo(String RqUID, Instant RqTm, String ScName, String clientId) {

        try {
            Optional<Client> client = clientRepository.findClientById(Long.valueOf(clientId));
            if (client.isPresent()) {
                Client clientEntity = client.get();
                Payload payload = maskClientData(clientEntity);
                logRequestAndResponse(RqUID, RqTm, ScName, clientId);
                LogModel logModel = maskClientDataLog(clientEntity);

                log.info("Response sent with status '200' - Payload: {}", logModel);

                Status status = new Status(STATUS_SUCCESS_CODE, STATUS_SUCCESS_NAME);
                status.setDescription(STATUS_SUCCESS_DESCRIPTION);

                GetClientInfo getClientInfo = new GetClientInfo();
                getClientInfo.setStatus(status);
                getClientInfo.setPayload(payload);

                return getClientInfo;
            } else {

                log.error("Client not found - Client ID: {}, RqUID: {}", clientId, RqUID);
                Status status1 = buildStatus(STATUS_BAD_REQUEST_CODE, STATUS_BAD_REQUEST_NAME, STATUS_BAD_REQUEST_DESCRIPTION);
                log.error("Bad Request - RqUID: {}", status1);
                return new GetClientInfo(status1);

            }

        }
        catch (Exception e){
            throw e;
        }

    }

    public Payload maskClientData(Client client) {
        return new Payload()
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .middleName(client.getMiddleName())
                .birthDate(Date.from(client.getBirthday()))
                .birthPlace(client.getBirthPlace());
    }

    private Status buildStatus(String code, String name, String description) {
        Status status = new Status();
        status.setCode(code);
        status.setName(name);
        status.setDescription(description);
        return status;
    }

    private void logRequestAndResponse(String RqUID, Instant RqTm, String ScName, String clientId) {
        log.info("Request received - RqUID: {}, RqTm: {}, ScName: {}, Client ID: {}", RqUID, RqTm, ScName, clientId);
    }

    public LogModel maskClientDataLog(Client client) {
        LogModel logModel = new LogModel();
        logModel.setFirstName(maskData(client.getFirstName()));
        logModel.setLastName(maskData(client.getLastName()));
        logModel.setMiddleName(maskData(client.getMiddleName()));
        logModel.setBirthday(maskData(String.valueOf(client.getBirthday())));
        logModel.setBirthPlace(maskData(client.getBirthPlace()));
        return logModel;
    }

    private String maskData(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }

        String maskedValue = value.substring(0, 1);
        for (int i = 1; i < value.length(); i++) {
            maskedValue += "*";
        }

        return maskedValue;
    }

}
