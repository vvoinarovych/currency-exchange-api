package com.sda.currencyexchangeapi.rest.exception;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExchangeRateProcessingError.class)
    public ErrorResponse handleWeatherProcessingException(final ExchangeRateProcessingError exception) {
        log.debug("Exception occurred");
        return new ErrorResponse(exception.getMessage());
    }
}
