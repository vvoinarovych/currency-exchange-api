package com.sda.currencyexchangeapi.service;
import com.sda.currencyexchangeapi.model.ExchangeRate;
import com.sda.currencyexchangeapi.model.ExchangeRateDto;
import com.sda.currencyexchangeapi.repo.ExchangeRateRepository;
import com.sda.currencyexchangeapi.service.ExchangeRateServiceImpl;
import com.sda.currencyexchangeapi.service.exchange_rate_client.ExchangeRateClient;
import com.sda.currencyexchangeapi.service.exchange_rate_client.ExchangeRateHostExchangeClient;
import com.sda.currencyexchangeapi.utils.ExchangeRateMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateServiceTest {

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Mock
    private ExchangeRateMapper mapper;

    @Mock
    private ExchangeRateHostExchangeClient exchangeRateClient;

    @Mock
    private Map<String, ExchangeRateClient> exchangeRateClientMap;

    @InjectMocks
    private ExchangeRateServiceImpl exchangeRateService;


    @Test
    void saveOrUpdateShouldWriteExchangeRateToDBIfNoRatesForThatDateAndCurrencies() {
        ExchangeRate exchangeRate = ExchangeRate.builder()
                .withRate(5.22)
                .withBaseCurrency("PLN")
                .withTargetCurrency("EUR")
                .withId(1)
                .withEffectiveDate(LocalDate.parse("2020-05-10"))
                .build();
        when(exchangeRateRepository.findByBaseCurrencyAndTargetCurrencyAndEffectiveDate(exchangeRate.getBaseCurrency(), exchangeRate.getTargetCurrency(), exchangeRate.getEffectiveDate())).thenReturn(null);

        ExchangeRate actual = exchangeRateService.saveOrUpdate(exchangeRate);

        assertThat(actual).isEqualTo(exchangeRate);
    }

    @Test
    void saveOrUpdateShouldUpdateExchangeRateInDBIfThereIsRecordWithThatDateAndCurrency() {
        ExchangeRate exchangeRate = ExchangeRate.builder()
                .withRate(1.22)
                .withBaseCurrency("USD")
                .withTargetCurrency("ZAR")
                .withId(1)
                .withEffectiveDate(LocalDate.parse("1445-05-10"))
                .build();
        ExchangeRate exchangeRateFromDb = ExchangeRate.builder()
                .withRate(9.22)
                .withBaseCurrency("USD")
                .withTargetCurrency("ZAR")
                .withId(1)
                .withEffectiveDate(LocalDate.parse("1445-05-10"))
                .build();
        when(exchangeRateRepository.findByBaseCurrencyAndTargetCurrencyAndEffectiveDate(
                exchangeRate.getBaseCurrency(),
                exchangeRate.getTargetCurrency(),
                exchangeRate.getEffectiveDate()
        )).thenReturn(exchangeRateFromDb);

        ExchangeRate actual = exchangeRateService.saveOrUpdate(exchangeRate);

        assertThat(actual).isEqualTo(exchangeRate);
        assertThat(actual).isSameAs(exchangeRateFromDb);
    }

    @Test
    void shouldSaveHistoricRateToDbIfNoSuchRecord(){
        ExchangeRate exchangeRate = ExchangeRate.builder()
                .withRate(11.1)
                .withBaseCurrency("CHF")
                .withTargetCurrency("PLN")
                .withId(1)
                .withEffectiveDate(LocalDate.parse("2007-05-10"))
                .build();
        ExchangeRateDto expected = new ExchangeRateDto(exchangeRate.getBaseCurrency(),exchangeRate.getTargetCurrency(),exchangeRate.getRate(),exchangeRate.getEffectiveDate());
        when(exchangeRateRepository.findByBaseCurrencyAndTargetCurrencyAndEffectiveDate(anyString(),anyString(),any())).thenReturn(null);
        when(mapper.toDto(exchangeRate)).thenReturn(expected);
        when(exchangeRateClient.getHistoricalExchangeRate(anyString(),anyString(),any())).thenReturn(exchangeRate);
        when(exchangeRateClientMap.getOrDefault(anyString(), any())).thenReturn(exchangeRateClient);

        ExchangeRateDto actual = exchangeRateService.getHistoricalExchangeRate("CHF","PLN","2007-05-10");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldReturnFromDbIfSuchRecordExists(){
        ExchangeRate exchangeRateFromDB = ExchangeRate.builder()
                .withRate(11.1)
                .withBaseCurrency("CHF")
                .withTargetCurrency("PLN")
                .withId(1)
                .withEffectiveDate(LocalDate.parse("2007-05-10"))
                .build();
        ExchangeRateDto expected = new ExchangeRateDto(exchangeRateFromDB.getBaseCurrency(),exchangeRateFromDB.getTargetCurrency(),exchangeRateFromDB.getRate(),exchangeRateFromDB.getEffectiveDate());
        when(exchangeRateRepository.findByBaseCurrencyAndTargetCurrencyAndEffectiveDate(anyString(),anyString(),any())).thenReturn(exchangeRateFromDB);
        when(mapper.toDto(exchangeRateFromDB)).thenReturn(expected);

        ExchangeRateDto actual = exchangeRateService.getHistoricalExchangeRate("CHF","PLN","2007-05-10");

        assertThat(actual).isEqualTo(expected);
    }

}
