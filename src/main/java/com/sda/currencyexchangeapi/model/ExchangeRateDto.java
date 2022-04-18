package com.sda.currencyexchangeapi.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ExchangeRateDto {

    private String base;
    private String target;
    private Double rate;
    private LocalDate date;

    public ExchangeRateDto(String base, String target, Double rate, LocalDate date) {
        this.base = base;
        this.target = target;
        this.rate = rate;
        this.date = date;
    }
}
