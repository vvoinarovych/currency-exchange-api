package com.sda.currencyexchangeapi.service;
import com.sda.currencyexchangeapi.model.ExchangeRate;
import com.sda.currencyexchangeapi.model.ExchangeRateDto;
import com.sda.currencyexchangeapi.repo.ExchangeRateRepository;
import com.sda.currencyexchangeapi.rest.exception.ExchangeRateProcessingError;
import com.sda.currencyexchangeapi.service.exchange_rate_client.ExchangeRateClient;
import com.sda.currencyexchangeapi.utils.ExchangeRateMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Log4j2
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final Map<String, ExchangeRateClient> exchangeRateClientMap;
    private final ExchangeRateMapper exchangeRateMapper;

    @Autowired
    public ExchangeRateServiceImpl(ExchangeRateRepository exchangeRateRepository,
                                   Map<String, ExchangeRateClient> exchangeRateClientMap,
                                   ExchangeRateMapper exchangeRateMapper) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.exchangeRateClientMap = exchangeRateClientMap;
        this.exchangeRateMapper = exchangeRateMapper;
    }

    @Override
    @Transactional
    public ExchangeRateDto getCurrentExchangeRate(String baseCurrency, String targetCurrency) {
        ExchangeRate exchangeRate = getExchangeRateClient(baseCurrency)
                .getCurrentExchangeRate(baseCurrency, targetCurrency);
        saveOrUpdate(exchangeRate);
        return exchangeRateMapper.toDto(exchangeRate);
    }

    @Override
    @Transactional
    public ExchangeRateDto getHistoricalExchangeRate(String baseCurrency, String targetCurrency, String date) {

        ExchangeRate exchangeRateFromDb = exchangeRateRepository.findByBaseCurrencyAndTargetCurrencyAndEffectiveDate(baseCurrency, targetCurrency, LocalDate.parse(date));
        if (exchangeRateFromDb == null) {
            ExchangeRate exchangeRate = getExchangeRateClient(baseCurrency)
                    .getHistoricalExchangeRate(baseCurrency, targetCurrency, date);
            exchangeRateRepository.save(exchangeRate);
            return exchangeRateMapper.toDto(exchangeRate);
        } else {
            return exchangeRateMapper.toDto(exchangeRateFromDb);
        }
    }

    @Override
    public List<ExchangeRateDto> getTimeSeriesExchangeRate(String baseCurrency, String targetCurrency, String startDate, String endDate) {
        List<ExchangeRate> rates = exchangeRateRepository.findExchangeRatesByEffectiveDateBetweenAndBaseCurrencyAndTargetCurrency(
                LocalDate.parse(startDate), LocalDate.parse(endDate), baseCurrency.toUpperCase(), targetCurrency.toUpperCase()
        );
        List<ExchangeRateDto>  exchangeRateDtoList = rates.stream()
                .sorted(Comparator.comparing(ExchangeRate::getEffectiveDate))
                .map(exchangeRateMapper::toDto)
                .collect(Collectors.toList());
        if(exchangeRateDtoList.isEmpty()){
            throw new ExchangeRateProcessingError("No data for that period");
        }
        return exchangeRateDtoList;
    }
    private ExchangeRateClient getExchangeRateClient(String baseCurrency) {
        return exchangeRateClientMap.getOrDefault(baseCurrency.toUpperCase(), exchangeRateClientMap.get("DEFAULT"));
    }

    private ExchangeRate saveOrUpdate(ExchangeRate exchangeRate) {
        ExchangeRate result = exchangeRateRepository.findByBaseCurrencyAndTargetCurrencyAndEffectiveDate(
                exchangeRate.getBaseCurrency(),
                exchangeRate.getTargetCurrency(),
                exchangeRate.getEffectiveDate()
        );
        if (result == null) {
            exchangeRateRepository.save(exchangeRate);
        } else {
            result.setRate(exchangeRate.getRate());
            exchangeRateRepository.save(result);
        }
        return result;
    }
}
