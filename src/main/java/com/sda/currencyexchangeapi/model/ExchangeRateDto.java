package com.sda.currencyexchangeapi.model;

import java.time.LocalDate;

public class ExchangeRateDto {

    private Double rate;
    private LocalDate date;

    public ExchangeRateDto(Double rate, LocalDate date) {
        this.rate = rate;
        this.date = date;
    }
}
