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
public class NbpExchangeClient implements ExchangeRateClient {

    private final static String CURRENT_EXCHANGE_RATES = "https://api.nbp.pl/api/exchangerates/rates/a/%s?format=json";
    private final static String HISTORICAL_EXCHANGE_RATES = "https://api.nbp.pl/api/exchangerates/rates/a/%s/%s?format=json";

    @Override
    public ExchangeRate getCurrentExchangeRate(String base, String target) {
        String urlString = String.format(CURRENT_EXCHANGE_RATES,  target);
        return buildRate(base, buildNode(urlString));
    }

    @Override
    public ExchangeRate getHistoricalExchangeRate(String base, String target, String date) {
        String urlString = String.format(HISTORICAL_EXCHANGE_RATES, target, date);
        return buildRate(base, buildNode(urlString));
    }

    public ObjectNode buildNode(String urlString) {
        try {
            URL url = new URL(urlString);
            ObjectNode node = new ObjectMapper().readValue(url, ObjectNode.class);
            log.info("NBP client used, url: " + url);
            return node;
        } catch (IOException e) {
            throw new ExchangeRateProcessingError("Wrong api call");
        }
    }

    private ExchangeRate buildRate(String base, ObjectNode node) {

        return ExchangeRate.builder()
                .withRate(Utility.round((1 / node.get("rates").get(0).get("mid").asDouble()), 4))
                .withBaseCurrency(base.toUpperCase())
                .withTargetCurrency(node.get("code").asText())
                .withEffectiveDate(LocalDate.parse(node.get("rates").get(0).get("effectiveDate").asText()))
                .build();
    }
}
