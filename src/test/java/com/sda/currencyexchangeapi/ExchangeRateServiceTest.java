package com.sda.currencyexchangeapi;

import com.sda.currencyexchangeapi.model.ExchangeRate;
import com.sda.currencyexchangeapi.model.ExchangeRateDto;
import com.sda.currencyexchangeapi.repo.ExchangeRateRepository;
import com.sda.currencyexchangeapi.service.ExchangeRateServiceImpl;
import com.sda.currencyexchangeapi.service.exchange_rate_client.ExchangeRateClient;
import com.sda.currencyexchangeapi.service.exchange_rate_client.NbpExchangeClient;
import com.sda.currencyexchangeapi.utils.ExchangeRateMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateServiceTest {

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Mock
    private ExchangeRateMapper exchangeRateMapper;

    @Mock
    private NbpExchangeClient nbpExchangeClient;

    @InjectMocks
    private ExchangeRateServiceImpl exchangeRateService;

    @Test
    void getCurrentExchangeRateTest() {

        //given
        String base = "PLN";
        String target = "USD";
        ExchangeRate forMockito = ExchangeRate.builder()
                .withBaseCurrency(base)
                .withTargetCurrency(target)
                .withRate(4.12)
                .withEffectiveDate(LocalDate.now())
                .build();
        ExchangeRateDto forMockitoDto = new ExchangeRateDto(forMockito.getBaseCurrency(), forMockito.getTargetCurrency(), forMockito.getRate(), forMockito.getEffectiveDate());

        //when
        when(exchangeRateService.getExchangeRateClient(base)).thenReturn(nbpExchangeClient);
        when(nbpExchangeClient.getCurrentExchangeRate(base, target)).thenReturn(forMockito);
        when(exchangeRateMapper.toDto(forMockito)).thenReturn(forMockitoDto);

        ExchangeRate exchangeRate = exchangeRateService.getExchangeRateClient(base)
                .getCurrentExchangeRate(base, target);

        //invoking saveOrUpdateMethod
        when(exchangeRateRepository.findByBaseCurrencyAndTargetCurrencyAndEffectiveDate(exchangeRate.getBaseCurrency(), exchangeRate.getTargetCurrency(), exchangeRate.getEffectiveDate()))
                .thenReturn(exchangeRate);
        when(exchangeRateRepository.save(any(ExchangeRate.class))).then(returnsFirstArg());
        ExchangeRate rateFromDb = exchangeRateRepository.findByBaseCurrencyAndTargetCurrencyAndEffectiveDate(exchangeRate.getBaseCurrency(), exchangeRate.getTargetCurrency(), exchangeRate.getEffectiveDate());
        ExchangeRate actualAfterSaveOrUpdate = exchangeRateRepository.save(rateFromDb);

        ExchangeRateDto actualDto = exchangeRateMapper.toDto(exchangeRate);

        //then
        assertThat(actualDto).isEqualTo(forMockitoDto);
        assertThat(actualAfterSaveOrUpdate).isEqualTo(exchangeRate);
    }
}
