package com.sda.currencyexchangeapi.utils;

import com.sda.currencyexchangeapi.model.ExchangeRate;
import com.sda.currencyexchangeapi.model.ExchangeRateDto;
import org.springframework.stereotype.Component;

@Component
public class ExchangeRateMapper {

    public ExchangeRateDto toDto(ExchangeRate exchangeRate){
        return new ExchangeRateDto(
                exchangeRate.getBaseCurrency(),
                exchangeRate.getTargetCurrency(),
                exchangeRate.getRate(),
                exchangeRate.getEffectiveDate()
        );
    }
}
