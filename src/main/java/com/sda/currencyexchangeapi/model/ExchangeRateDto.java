package com.sda.currencyexchangeapi.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ExchangeRateDto {

    private Double rate;
    private LocalDate date;

    public ExchangeRateDto(Double rate, LocalDate date) {
        this.rate = rate;
        this.date = date;
    }
}
