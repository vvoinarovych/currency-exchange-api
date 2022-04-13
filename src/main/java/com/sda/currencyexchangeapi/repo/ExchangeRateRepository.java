package com.sda.currencyexchangeapi.repo;

import com.sda.currencyexchangeapi.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Integer> {

    ExchangeRate findByBaseCurrencyAndTargetCurrencyAndEffectiveDate(String baseCurrency,
                                                                     String targetCurrency,
                                                                     LocalDate effectiveDate);
}
