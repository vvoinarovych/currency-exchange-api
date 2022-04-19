package com.sda.currencyexchangeapi.service.exchange_rate_client;

import com.sda.currencyexchangeapi.model.ExchangeRate;
import org.springframework.stereotype.Component;

@Component
public interface ExchangeRateClient {

    ExchangeRate getCurrentExchangeRate(String base, String target);

    ExchangeRate getHistoricalExchangeRate(String base, String target, String data);


}
