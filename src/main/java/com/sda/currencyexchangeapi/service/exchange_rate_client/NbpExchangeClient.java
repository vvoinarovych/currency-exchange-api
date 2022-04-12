package com.sda.currencyexchangeapi.service.exchange_rate_client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sda.currencyexchangeapi.model.ExchangeRate;
import com.sda.currencyexchangeapi.utils.Utility;
import lombok.extern.log4j.Log4j2;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;

@Log4j2
public class NbpExchangeClient implements ExchangeRateClient{

    private final String CURRENT_EXCHANGE_RATES = "https://api.nbp.pl/api/exchangerates/rates/c/%s?format=json";
    @Override
    public ExchangeRate getCurrentExchangeRate(String base, String target) {
        ExchangeRate exchangeRate = null;
        try {
            URL url = new URL(String.format(CURRENT_EXCHANGE_RATES, target));
            ObjectNode node = new ObjectMapper().readValue(url, ObjectNode.class);
            exchangeRate = buildRate(node);
        }catch (IOException e) {
            log.error(e);
        }
        return exchangeRate;
    }

    private ExchangeRate buildRate(ObjectNode node) {
        Double ask = node.get("rates").get(0).get("ask").asDouble();
        Double bid = node.get("rates").get(0).get("bid").asDouble();
        Double average = (ask + bid)/2;
        Double rate = Utility.round(average,4);

        return ExchangeRate.builder()
                .withRate(rate)
                .withBaseCurrency("PLN")
                .withTargetCurrency(node.get("code").asText())
                .withEffectiveDate(LocalDate.parse(node.get("rates").get(0).get("effectiveDate").asText()))
                .build();
    }
}
