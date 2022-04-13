package com.sda.currencyexchangeapi.service;
import com.sda.currencyexchangeapi.model.ExchangeRate;
import com.sda.currencyexchangeapi.model.ExchangeRateDto;
import com.sda.currencyexchangeapi.repo.ExchangeRateRepository;
import com.sda.currencyexchangeapi.service.exchange_rate_client.ExchangeRateClient;
import com.sda.currencyexchangeapi.utils.ExchangeRateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Map;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final Map<String, ExchangeRateClient> exchangeRateClientMap;
    private final ExchangeRateMapper exchangeRateMapper;

    @Autowired
    public ExchangeRateServiceImpl(ExchangeRateRepository exchangeRateRepository, Map<String, ExchangeRateClient> exchangeRateClientMap, ExchangeRateMapper exchangeRateMapper) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.exchangeRateClientMap = exchangeRateClientMap;
        this.exchangeRateMapper = exchangeRateMapper;
    }

    @Override
    @Transactional
    public ExchangeRateDto getCurrentExchangeRate(String baseCurrency, String targetCurrency) {
        ExchangeRate exchangeRate = exchangeRateClientMap
                .getOrDefault(baseCurrency.toUpperCase(), exchangeRateClientMap.get("WORLD"))
                .getCurrentExchangeRate(baseCurrency, targetCurrency);
        exchangeRateRepository.save(exchangeRate);
        return exchangeRateMapper.toDto(exchangeRate);
    }
}
