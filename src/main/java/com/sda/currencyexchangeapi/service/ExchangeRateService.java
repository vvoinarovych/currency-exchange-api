package com.sda.currencyexchangeapi.service;

import com.sda.currencyexchangeapi.model.ExchangeRate;

public interface ExchangeRateService {

    ExchangeRate getCurrentExchangeRate(String baseCurrency, String targetCurrency);

}
