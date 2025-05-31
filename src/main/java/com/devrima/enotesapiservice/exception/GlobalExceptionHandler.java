package com.devrima.enotesapiservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerException(Exception e){
        log.error ( "GlobalExceptionHandler :: handlerException ", e.getMessage () );
        return new ResponseEntity<> ( e.getMessage (),HttpStatus.INTERNAL_SERVER_ERROR );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handlerResourceNotFoundException(ResourceNotFoundException e){
        log.error ( "GlobalExceptionHandler :: handlerResourceNotFoundException",e.getMessage () );
        return new ResponseEntity<> ( e.getMessage (), HttpStatus.NOT_FOUND );
    }


}
