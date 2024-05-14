package org.example.myproject1.exceptionHandler;

import jakarta.validation.ConstraintViolationException;
import org.example.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {


    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ResponseEntity<Status> handleDataAccessResourceFailure(DataAccessResourceFailureException ex, WebRequest request) {
        Status status = new Status();
        status.setCode("4");
        status.setName("Timeout error");
        status.setDescription("TimeOut");
        String rquid = request.getHeader("x-platform-rquid");
        String rqtm = request.getHeader("x-platform-rqtm");
        String scName = request.getHeader("x-platform-scname");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-platform-rquid",rquid);
        httpHeaders.add("x-platform-rqtm",rqtm);
        log.error("Time out " + rquid + " " + rqtm + " " + scName + "Exception " + status );
        return new ResponseEntity<>(status,httpHeaders,HttpStatus.GATEWAY_TIMEOUT);
    }


    @ExceptionHandler({MethodArgumentTypeMismatchException.class,ConstraintViolationException.class})
    public ResponseEntity<Status> handleConstraint(Exception ex,  WebRequest request){
        Status status = new Status();
        status.setCode("1");
        status.setName("Not valid request");
        status.setDescription("Required field is missing");
        String rquid = request.getHeader("x-platform-rquid");
        String rqtm = request.getHeader("x-platform-rqtm");
        String scName = request.getHeader("x-platform-scname");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-platform-rquid",rquid);
        httpHeaders.add("x-platform-rqtm",rqtm);
        log.error("Bad request " + rquid + " " + rqtm + " " + scName + "Exception " + status );
        return new ResponseEntity<>(status,httpHeaders,HttpStatus.BAD_REQUEST);

    }

}
