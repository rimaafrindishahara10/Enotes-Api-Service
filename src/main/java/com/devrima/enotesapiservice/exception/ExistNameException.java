package com.devrima.enotesapiservice.exception;

public class ExistNameException  extends RuntimeException{

    public ExistNameException (String message){
        super(message);
    }

}
