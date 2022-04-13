package com.sda.currencyexchangeapi.service;

import com.sda.currencyexchangeapi.model.ExchangeRateDto;

public interface ExchangeRateService {

    ExchangeRateDto getCurrentExchangeRate(String baseCurrency, String targetCurrency);

}
