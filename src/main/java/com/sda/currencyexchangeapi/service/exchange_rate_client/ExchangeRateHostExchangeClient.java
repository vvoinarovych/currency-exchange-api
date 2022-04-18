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
@Component("DEFAULT")
public class ExchangeRateHostExchangeClient implements ExchangeRateClient {

    private final static String CURRENT_EXCHANGE_RATES = "https://api.exchangerate.host/latest?base=%s&symbols=%s";
    private final static String HISTORICAL_EXCHANGE_RATES = "https://api.exchangerate.host/%s?base=%s&symbols=%s";

    @Override
    public ExchangeRate getCurrentExchangeRate(String base, String target) {
        String urlString = String.format(CURRENT_EXCHANGE_RATES, base, target);
        return buildRate(target, buildNode(urlString));
    }

    @Override
    public ExchangeRate getHistoricalExchangeRate(String base, String target, String date) {
        String urlString = String.format(HISTORICAL_EXCHANGE_RATES, date, base, target);
        return buildRate(target, buildNode(urlString));
    }

    public ObjectNode buildNode(String urlString) {
        try {
            URL url = new URL(urlString);
            ObjectNode node = new ObjectMapper().readValue(url, ObjectNode.class);
            log.info("ExchangeRateHost client used, url: " + url);
            return node;
        } catch (IOException e) {
            throw new ExchangeRateProcessingError("Wrong api call");
        }
    }

    private ExchangeRate buildRate(String target, ObjectNode node) {
            return ExchangeRate.builder()
                    .withBaseCurrency(node.get("base").asText())
                    .withTargetCurrency(target.toUpperCase())
                    .withRate(Utility.round(node.get("rates").get(target.toUpperCase()).asDouble(), 4))
                    .withEffectiveDate(LocalDate.parse(node.get("date").asText()))
                    .build();
        }

}
