package com.sda.currencyexchangeapi.service;
import com.sda.currencyexchangeapi.model.ExchangeRate;
import com.sda.currencyexchangeapi.repo.ExchangeRateRepository;
import com.sda.currencyexchangeapi.service.exchange_rate_client.ExchangeRateClient;
import com.sda.currencyexchangeapi.service.exchange_rate_client.ExchangeRateHostExchangeClient;
import com.sda.currencyexchangeapi.service.exchange_rate_client.NbpExchangeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private ExchangeRateClient exchangeRateClient;

    @Autowired
    public ExchangeRateServiceImpl(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @Override
    public ExchangeRate getCurrentExchangeRate(String baseCurrency, String targetCurrency) {
        if (baseCurrency.equalsIgnoreCase("pln")) {
            exchangeRateClient = new NbpExchangeClient();
        } else {
            exchangeRateClient = new ExchangeRateHostExchangeClient();
        }
        ExchangeRate exchangeRate = exchangeRateClient.getCurrentExchangeRate(baseCurrency, targetCurrency);
        exchangeRateRepository.save(exchangeRate);

        return exchangeRate;
    }
}
