package com.sda.currencyexchangeapi;
import com.sda.currencyexchangeapi.model.ExchangeRate;
import com.sda.currencyexchangeapi.repo.ExchangeRateRepository;
import com.sda.currencyexchangeapi.service.ExchangeRateServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateServiceTest {

    @Mock
    private ExchangeRateRepository exchangeRateRepository;


    @InjectMocks
    private ExchangeRateServiceImpl exchangeRateService;


    @Test
    void saveOrUpdateShouldWriteExchangeRateToDBIfNoRatesForThatDateAndCurrencies() {
        ExchangeRate exchangeRate = ExchangeRate.builder()
                .withRate(5.22)
                .withBaseCurrency("PLN")
                .withTargetCurrency("EUR")
                .withId(1)
                .withEffectiveDate(LocalDate.parse("1945-05-10"))
                .build();
        when(exchangeRateRepository.findByBaseCurrencyAndTargetCurrencyAndEffectiveDate(anyString(), anyString(), any())).thenReturn(null);

        ExchangeRate actual = exchangeRateService.saveOrUpdate(exchangeRate);

        assertThat(actual).isEqualTo(exchangeRate);

    }
}
