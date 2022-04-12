package com.sda.currencyexchangeapi.service.exchange_rate_client;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sda.currencyexchangeapi.model.ExchangeRate;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;

@Log4j2
@Component("WORLD")
public class ExchangeRateHostExchangeClient implements ExchangeRateClient {

    private final String CURRENT_EXCHANGE_RATES = "https://api.exchangerate.host/latest?base=%s&symbols=%s";

    @Override
    public ExchangeRate getCurrentExchangeRate(String base, String target) {
        ExchangeRate exchangeRate = null;
        try {
            URL url = new URL(String.format(CURRENT_EXCHANGE_RATES, base, target));
            ObjectNode node = new ObjectMapper().readValue(url, ObjectNode.class);
            exchangeRate = buildRate(node, target);
        }catch (IOException e) {
            log.error(e);
        }
        log.info("ExchangeRateHost client used");
        return exchangeRate;
    }

    private ExchangeRate buildRate(ObjectNode node, String target) {
        return ExchangeRate.builder()
                .withBaseCurrency(node.get("base").asText())
                .withTargetCurrency(target)
                .withRate(node.get("rates").get(target).asDouble())
                .withEffectiveDate(LocalDate.parse(node.get("date").asText()))
                .build();
    }


}
