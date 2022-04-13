package com.sda.currencyexchangeapi.rest.exception;

public class ExchangeRateProcessingError extends RuntimeException{

    public ExchangeRateProcessingError(String message){
        super(message);
    }

}
