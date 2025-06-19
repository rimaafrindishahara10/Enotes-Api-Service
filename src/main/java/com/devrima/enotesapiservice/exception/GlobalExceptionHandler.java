package com.devrima.enotesapiservice.exception;

import com.devrima.enotesapiservice.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.FileNotFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerException(Exception e){
        log.error ( "GlobalExceptionHandler :: handlerException ", e.getMessage () );
        return CommonUtil.createErrorResponseMessage ( e.getMessage (),HttpStatus.INTERNAL_SERVER_ERROR );
//        return new ResponseEntity<> ( e.getMessage (),HttpStatus.INTERNAL_SERVER_ERROR );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handlerResourceNotFoundException(ResourceNotFoundException e){
        log.error ( "GlobalExceptionHandler :: handlerResourceNotFoundException",e.getMessage () );
        return  CommonUtil.createErrorResponseMessage ( e.getMessage (),HttpStatus.NOT_FOUND );
//        return new ResponseEntity<> ( e.getMessage (), HttpStatus.NOT_FOUND );
    }

    @ExceptionHandler(ExistNameException.class)
    public ResponseEntity<?> handlerExistNameException(ExistNameException e){
        log.error ( "GlobalExceptionHandler :: handlerExistNameException",e.getMessage () );
        return CommonUtil.createErrorResponseMessage ( e.getMessage (),HttpStatus.CONFLICT );
//        return new ResponseEntity<> ( e.getMessage (),HttpStatus.CONFLICT );
    }
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> handlerFileNotFoundException(FileNotFoundException e){
        log.error ( "GlobalExceptionHandler :: handlerFileNotFoundException",e.getMessage () );
        return CommonUtil.createErrorResponseMessage ( e.getMessage (),HttpStatus.CONFLICT );
//        return new ResponseEntity<> ( e.getMessage (),HttpStatus.CONFLICT );
    }


}
