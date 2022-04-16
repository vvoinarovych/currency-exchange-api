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

    private final String CURRENT_EXCHANGE_RATES = "https://api.exchangerate.host/latest?base=%s&symbols=%s";
    private final String HISTORICAL_EXCHANGE_RATES = "https://api.exchangerate.host/%s?base=%s&symbols=%s";

    @Override
    public ExchangeRate getCurrentExchangeRate(String base, String target) {
        try {
            URL url = new URL(String.format(CURRENT_EXCHANGE_RATES, base, target));
            ExchangeRate exchangeRate = buildRate(target, url);
            log.info("ExchangeRateHost client used, url: " + url);
            return exchangeRate;
        }catch (IOException e) {
            throw new ExchangeRateProcessingError("Could not get data for chosen currencies");
        }
    }

    @Override
    public ExchangeRate getHistoricalExchangeRate( String base, String target, String date) {
        try {
            URL url = new URL(String.format(HISTORICAL_EXCHANGE_RATES, date, base, target));
            ExchangeRate exchangeRate = buildRate(target, url);
            log.info("ExchangeRateHost client used, url: " + url);
            return  exchangeRate;
        }catch (IOException e) {
            throw new ExchangeRateProcessingError("Could not get data for chosen currencies");
        }
    }

    private ExchangeRate buildRate(String target, URL url) throws IOException {

        ObjectNode node = new ObjectMapper().readValue(url, ObjectNode.class);

        return ExchangeRate.builder()
                .withBaseCurrency(node.get("base").asText())
                .withTargetCurrency(target.toUpperCase())
                .withRate(Utility.round(node.get("rates").get(target.toUpperCase()).asDouble(),4))
                .withEffectiveDate(LocalDate.parse(node.get("date").asText()))
                .build();
    }
}
