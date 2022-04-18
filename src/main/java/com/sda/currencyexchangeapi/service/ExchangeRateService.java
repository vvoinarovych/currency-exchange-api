package com.sda.currencyexchangeapi.service;

import com.sda.currencyexchangeapi.model.ExchangeRateDto;

import java.util.List;

public interface ExchangeRateService {

    ExchangeRateDto getCurrentExchangeRate(String baseCurrency, String targetCurrency);
    ExchangeRateDto getHistoricalExchangeRate(String baseCurrency, String targetCurrency, String date);
    List<ExchangeRateDto> getTimeSeriesExchangeRate(String baseCurrency, String targetCurrency, String startDate, String endDate);


}
