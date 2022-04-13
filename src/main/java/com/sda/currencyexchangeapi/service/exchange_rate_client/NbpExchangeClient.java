package com.sda.currencyexchangeapi.service.exchange_rate_client;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sda.currencyexchangeapi.model.ExchangeRate;
import com.sda.currencyexchangeapi.rest.exception.ExchangeRateProcessingError;
import com.sda.currencyexchangeapi.utils.Utility;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;

@Log4j2
@Component("PLN")
public class NbpExchangeClient implements ExchangeRateClient{

    private final String CURRENT_EXCHANGE_RATES = "https://api.nbp.pl/api/exchangerates/rates/a/%s?format=json";
    private final String HISTORICAL_EXCHANGE_RATES = "https://api.nbp.pl/api/exchangerates/rates/a/%s/%s?format=json";
    @Override
    public ExchangeRate getCurrentExchangeRate(String base, String target) {
        ExchangeRate exchangeRate;
        try {
            URL url = new URL(String.format(CURRENT_EXCHANGE_RATES, target));
            ObjectNode node = new ObjectMapper().readValue(url, ObjectNode.class);
            exchangeRate = buildRate(node, base);
            log.info(url);
        }catch (IOException e) {
            throw new ExchangeRateProcessingError("Could not get data for chosen currencies");
        }
        log.info("NBP Exchange client used");
        return exchangeRate;
    }

    @Override
    public ExchangeRate getHistoricalExchangeRate(String base, String target, String date) {
        ExchangeRate exchangeRate = null;
        try {
            URL url = new URL(String.format(HISTORICAL_EXCHANGE_RATES, target, date));
            System.out.println(url);
            ObjectNode node = new ObjectMapper().readValue(url, ObjectNode.class);
            exchangeRate = buildRate(node, base);
        }catch (IOException e) {
            log.error(e);
        }
        log.info("NBP Exchange client for Historical Data used");
        return exchangeRate;
    }

    private ExchangeRate buildRate(ObjectNode node, String base) {
        double rate = node.get("rates").get(0).get("mid").asDouble();
        rate = Utility.round((1/rate),4);
        return ExchangeRate.builder()
                .withRate(rate)
                .withBaseCurrency(base.toUpperCase())
                .withTargetCurrency(node.get("code").asText())
                .withEffectiveDate(LocalDate.parse(node.get("rates").get(0).get("effectiveDate").asText()))
                .build();
    }
}
