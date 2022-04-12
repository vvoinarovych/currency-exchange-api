package com.sda.currencyexchangeapi.service;
import com.sda.currencyexchangeapi.model.ExchangeRate;
import com.sda.currencyexchangeapi.repo.ExchangeRateRepository;
import com.sda.currencyexchangeapi.service.exchange_rate_client.ExchangeRateClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Map;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final Map<String, ExchangeRateClient> exchangeRateClientMap;

    @Autowired
    public ExchangeRateServiceImpl(ExchangeRateRepository exchangeRateRepository, Map<String, ExchangeRateClient> exchangeRateClientMap) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.exchangeRateClientMap = exchangeRateClientMap;
    }

    @Override
    @Transactional
    public ExchangeRate getCurrentExchangeRate(String baseCurrency, String targetCurrency) {
        ExchangeRate exchangeRate = exchangeRateClientMap
                .getOrDefault(baseCurrency.toUpperCase(), exchangeRateClientMap.get("WORLD"))
                .getCurrentExchangeRate(baseCurrency, targetCurrency);
        exchangeRateRepository.save(exchangeRate);
        return exchangeRate;
    }
}
