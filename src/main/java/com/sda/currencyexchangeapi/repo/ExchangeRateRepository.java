package com.sda.currencyexchangeapi.repo;

import com.sda.currencyexchangeapi.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Integer> {

    ExchangeRate findByBaseCurrencyAndTargetCurrencyAndEffectiveDate(String baseCurrency,
                                                                     String targetCurrency,
                                                                     LocalDate effectiveDate);

    List<ExchangeRate> findExchangeRatesByEffectiveDateBetweenAndBaseCurrencyAndTargetCurrency(LocalDate effectiveDateStart,
                                                                                               LocalDate effectiveDateEnd,
                                                                                               String baseCurrency,
                                                                                               String targetCurrency);
}
