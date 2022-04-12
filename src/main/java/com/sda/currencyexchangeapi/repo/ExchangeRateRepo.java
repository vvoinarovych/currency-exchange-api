package com.sda.currencyexchangeapi.repo;

import com.sda.currencyexchangeapi.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRateRepo extends JpaRepository<ExchangeRate, Integer> {

}
