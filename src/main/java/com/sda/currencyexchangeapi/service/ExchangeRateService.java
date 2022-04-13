package com.sda.currencyexchangeapi.service;

import com.sda.currencyexchangeapi.model.ExchangeRate;
import com.sda.currencyexchangeapi.model.ExchangeRateDto;

public interface ExchangeRateService {

    ExchangeRateDto getCurrentExchangeRate(String baseCurrency, String targetCurrency);
    ExchangeRateDto getHistoricalExchangeRate(String baseCurrency, String targetCurrency, String date);


}
